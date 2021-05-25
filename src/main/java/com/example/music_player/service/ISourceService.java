package com.example.music_player.service;

import com.example.music_player.entity.Song;
import com.example.music_player.storage.StorageTypes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ISourceService {

      void save(MultipartFile multipartFile, Song song, Long songIdFromDB);

      byte[] findByName(String name, StorageTypes storage_type, String  file_type) throws IOException;

      boolean isExist(Long id);

      void delete(String name);
    //save many files for ZIP file
 //     ResponseEntity<String> saveFiles(MultipartFile[] files);
}
