package com.example.music_player.service;

import com.example.music_player.entity.Song;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

 public interface ISourceService {

      void save(MultipartFile multipartFile, Song song, Long songIdFromDB);

      ResponseEntity<byte[]> findById(Long id) throws IOException;

      boolean isExist(Long id);

      void delete(Long id);
    //save many files for ZIP file
      ResponseEntity<String> saveFiles(MultipartFile[] files);
}
