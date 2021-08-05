package com.example.music_player.service;

import com.example.music_player.entity.Song;
import com.example.music_player.entity.Source;
import com.example.music_player.repository.ISourceRepository;
import com.example.music_player.storage.IStorageSourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SourceService implements ISourceService {

    private final ISourceRepository sourceRepository;
    private final IStorageSourceService storageSourceService;

    @Autowired
    public SourceService(ISourceRepository sourceRepository, IStorageSourceService storageSourceService) {
        this.sourceRepository = sourceRepository;
        this.storageSourceService = storageSourceService;
    }

    @Transactional
    public Source save(MultipartFile multipartFile, Song song, Long songIdFromDB) {
        List<Source> sourceList = new ArrayList<>();

        try {
            InputStream inputStream = multipartFile.getInputStream();
            String fileName = multipartFile.getOriginalFilename();
            String contentType = multipartFile.getContentType();

//          if (!sourceRepository.isExistByNameAndFileType(song.getName(), contentType)) {
            if (!sourceRepository.isExistByChecksum(DigestUtils.md5Hex(inputStream))) {
                sourceList = storageSourceService.save(multipartFile.getInputStream(), fileName, contentType);
                sourceList.forEach((x) -> {
                    x.setSong_id(songIdFromDB);
                    sourceRepository.save(x);
                    log.info("file " + x.getName() + " save in source repository");
                });
            } else {
                log.info("file " + song.getName() + " is exist at this moment in DB ");
            }
        } catch (IOException e) {
            log.error("EXCEPTION IN: SourceService save()" + e.getMessage());
        }
        return sourceList.stream().findAny().orElseThrow(() -> new IllegalStateException("source do not fined"));
    }

    public byte[] findByName(String name, String storage_type, String file_type) throws IOException {
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

    public Boolean delete(String name) {
        List<Source> sourceList = Optional.ofNullable(sourceRepository.findAllByName(name))//TODO list by names
                .orElseThrow(() -> new IllegalStateException("source with " + name + " do not fined"));
        sourceList.forEach((source) -> {
            storageSourceService.delete(source);
            sourceRepository.deleteById(source.getId());
        });
        return true;
    }
}
