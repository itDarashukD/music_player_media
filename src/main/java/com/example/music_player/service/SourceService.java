package com.example.music_player.service;

import com.example.music_player.entity.Song;
import com.example.music_player.entity.Source;
import com.example.music_player.repository.ISourceRepository;
import com.example.music_player.storage.IStorageSourceService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class SourceService implements ISourceService{

    private final IStorageSourceService storageSourceService;
    private final ISourceRepository sourceRepository;

    @Autowired
    public SourceService(IStorageSourceService storageSourceService
            , ISourceRepository sourceRepository) {
        this.storageSourceService = storageSourceService;
        this.sourceRepository = sourceRepository;
    }

    @Transactional
    public void save(MultipartFile multipartFile, Song song, Long songIdFromDB) {
        if (!sourceRepository.isExistByName(song.getName())) {
            sourceRepository.save(storageSourceService.save(multipartFile, songIdFromDB));
        } else {
            System.out.println("file " + song.getName() + " in DB is Exist at this moment");
        }
    }

    public ResponseEntity<byte[]> findById(Long id) throws IOException {
        Source source = Optional.ofNullable(sourceRepository.findById(id))
                .orElseThrow(() -> new IllegalStateException("source with " + id + " do not fined"));
        byte[] content = IOUtils.toByteArray(storageSourceService.findSongBySource(source));
        return ResponseEntity
                .ok()
                .contentLength(content.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + source.getName() + "\"")
                .body(content);
    }

    public boolean isExist(Long id) {
        Source source = Optional.ofNullable(sourceRepository.findById(id))
                .orElseThrow(() -> new IllegalStateException("source with " + id + " do not fined"));
        return storageSourceService.isExist(source);
    }

    // @Transactional
    public void delete(Long id) {
        Source source = Optional.ofNullable(sourceRepository.findById(id))
                .orElseThrow(() -> new IllegalStateException("source with " + id + " do not fined"));
        storageSourceService.delete(source);
        sourceRepository.deleteById(id);
    }

    //save many files for ZIP file
    public ResponseEntity<String> saveFiles(MultipartFile[] files) {
        List<String> fileNames = new ArrayList<>();
        try {
            Arrays.stream(files).forEach(file -> {
                storageSourceService.saveZip(file.getResource()
                        , file.getOriginalFilename()
                        , file.getContentType());

                fileNames.add(file.getOriginalFilename());
            });
            String message = "Uploaded the files successfully: " + fileNames;
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            String message = "Fail to upload files!";
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }
    }
}

