package com.example.music_player.service;

import com.example.music_player.entity.Song;
import com.example.music_player.entity.Source;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ISourceService {

    Source save(MultipartFile multipartFile, Song song, Long songIdFromDB);

    byte[] findByName(String name, String storage_type, String file_type) throws IOException;

    boolean isExist(Long id);

    void delete(String name);
}
