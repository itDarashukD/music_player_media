package com.example.music_player.service;

import com.example.music_player.entity.Song;
import com.example.music_player.entity.Source;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Slf4j
public class DecoratorQueueService implements ISourceService {

    @Value("${activemq.queue.name}")
    private ActiveMQQueue queue;
    private final ISourceService sourceService;
    @Autowired
    private JmsTemplate jmsTemplate;

    public DecoratorQueueService(ISourceService sourceService) {
        this.sourceService = sourceService;
    }

    @Override
    public Source save(MultipartFile multipartFile, Song song, Long songIdFromDB) {
        log.info("IN DecoratorQueueService save() : method are started ");
        Source source = sourceService.save(multipartFile, song, songIdFromDB);
        String contentType = source.getFileType();

        if (!Objects.requireNonNull(contentType).equals("audio/mpeg")) {
            jmsTemplate.convertAndSend(queue, source);
            log.info("file " + source.getName() + "was put in Queue ");
        }
        return source;
    }

    @Override
    public byte[] findByName(String name, String storage_type, String file_type) throws IOException {
        return sourceService.findByName(name, storage_type, file_type);
    }

    @Override
    public boolean isExist(Long id) {
        return sourceService.isExist(id);
    }

    @Override
    public void delete(String name) {
        sourceService.delete(name);
    }
}
