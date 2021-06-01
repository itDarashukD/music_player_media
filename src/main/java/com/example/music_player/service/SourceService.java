package com.example.music_player.service;

import com.example.music_player.entity.Song;
import com.example.music_player.entity.Source;
import com.example.music_player.repository.ISourceRepository;
import com.example.music_player.storage.IStorageSourceService;
import com.example.music_player.storage.StorageTypes;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class SourceService implements ISourceService {

    @Value("${activemq.queue.name}")
    private ActiveMQQueue queue;

    private final ISourceRepository sourceRepository;
    private final IStorageSourceService storageSourceService;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    public SourceService(ISourceRepository sourceRepository, IStorageSourceService storageSourceService) {
        this.sourceRepository = sourceRepository;
        this.storageSourceService = storageSourceService;
    }

    @Transactional
    public void save(MultipartFile multipartFile, Song song, Long songIdFromDB) {
        try {
            InputStream inputStream = multipartFile.getInputStream();
            String fileName = multipartFile.getOriginalFilename();
            String contentType = multipartFile.getContentType();

            if (!sourceRepository.isExistByNameAndFileType(song.getName(), contentType)) {
                List<Source> source = storageSourceService.save(inputStream, fileName, contentType);
                source.forEach((x) -> {
                    x.setSong_id(songIdFromDB);
                    sourceRepository.save(x);
                    log.info("file " + x.getName() + " save in source repository");
                });

                if (!Objects.requireNonNull(contentType).equals("audio/mpeg")) {
                    putSourceToQueue(source.get(0));
                }

            } else {
                log.info("file " + song.getName() + " in DB is Exist at this moment");
            }
        } catch (IOException e) {
            log.error("Exeption IN: save()" + e.getMessage());
        }
    }

    public void putSourceToQueue(Source sourceToQueue) {
        jmsTemplate.convertAndSend(queue, sourceToQueue);
        log.info("IN putSourceToQueue: " + sourceToQueue + " was put in ActiveMQ queue!");
    }

    public byte[] findByName(String name, StorageTypes storage_type, String file_type) throws IOException {
        Source source = Optional.ofNullable(sourceRepository.findByNameAndStorageType(name, storage_type, file_type))
                .orElseThrow(() -> new IllegalStateException("source with " + name + " do not fined"));
        source.setStorage_types(storage_type);

        return IOUtils.toByteArray(storageSourceService.findSongBySource(source));
    }

    public boolean isExist(Long id) {
        Source source = Optional.ofNullable(sourceRepository.findById(id))
                .orElseThrow(() -> new IllegalStateException("source with " + id + " do not fined"));
        return storageSourceService.isExist(source);
    }

    public void delete(String name) {
        List<Source> sourceList = Optional.ofNullable(sourceRepository.findAllByName(name))//TODO list by names
                .orElseThrow(() -> new IllegalStateException("source with " + name + " do not fined"));
        sourceList.forEach((source) -> {
            storageSourceService.delete(source);
            sourceRepository.deleteById(source.getId());
        });
    }

}
