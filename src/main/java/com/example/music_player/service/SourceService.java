package com.example.music_player.service;

import com.example.music_player.entity.Song;
import com.example.music_player.entity.Source;
import com.example.music_player.repository.ISourceRepository;
import com.example.music_player.storage.IStorageSourceService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class SourceService implements ISourceService {

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
        try {
            InputStream inputStream = multipartFile.getInputStream();
            String fileName = multipartFile.getOriginalFilename();
            String contentType = multipartFile.getContentType();
            if (!sourceRepository.isExistByName(song.getName())) {
                Source source = storageSourceService.save(inputStream, fileName, contentType);
                source.setSong_id(songIdFromDB);
                sourceRepository.save(source);
            } else {
                System.out.println("file " + song.getName() + " in DB is Exist at this moment");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public byte[] findByName(String name, Long storageTypeId) throws IOException {
        Source source = Optional.ofNullable(sourceRepository.findByName(name))
                .orElseThrow(() -> new IllegalStateException("source with " + name + " do not fined"));
        source.setStorage_id(storageTypeId);
        return IOUtils.toByteArray(storageSourceService.findSongBySource(source));
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
//    //save many files for ZIP file
//    public ResponseEntity<String> saveFiles(MultipartFile[] files) {
//        List<String> fileNames = new ArrayList<>();
//        try {
//            Arrays.stream(files).forEach(file -> {
//                storageSourceService.saveZip(file.getResource()
//                        , file.getOriginalFilename()
//                        , file.getContentType());
//
//                fileNames.add(file.getOriginalFilename());
//            });
//            String message = "Uploaded the files successfully: " + fileNames;
//            return ResponseEntity.status(HttpStatus.OK).body(message);
//        } catch (Exception e) {
//            String message = "Fail to upload files!";
//            return ResponseEntity.status(HttpStatus.OK).body(message);
//        }
//    }
}

