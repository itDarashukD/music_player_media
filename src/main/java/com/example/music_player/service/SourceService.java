package com.example.music_player.service;

import com.example.music_player.entity.Song;
import com.example.music_player.entity.Source;
import com.example.music_player.repository.ISourceRepository;
import com.example.music_player.storage.IStorageSourceService;
import com.example.music_player.storage.StorageRouter;
import com.example.music_player.storage.StorageTypes;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class SourceService implements ISourceService {

    private final ISourceRepository sourceRepository;
  //  private final StorageRouter storageRouter;
    private final IStorageSourceService storageSourceService;
//    @Autowired
//    public SourceService(ISourceRepository sourceRepository, StorageRouter storageRouter) {
//        this.sourceRepository = sourceRepository;
//        this.storageRouter = storageRouter;
//    }

        @Autowired
    public SourceService(ISourceRepository sourceRepository,  IStorageSourceService storageSourceService) {
        this.sourceRepository = sourceRepository;
        this.storageSourceService = storageSourceService;
    }

    @Transactional
    public void save(MultipartFile multipartFile, Song song, Long songIdFromDB) {
        try {
            InputStream inputStream = multipartFile.getInputStream();
            String fileName = multipartFile.getOriginalFilename();
            String contentType = multipartFile.getContentType();
            if (!sourceRepository.isExistByName(song.getName())) {

            List<Source> source=  storageSourceService.save(inputStream, fileName, contentType);
                source.forEach((x) -> {
                    x.setSong_id(songIdFromDB);
                    sourceRepository.save(x);
                });
            } else {
                System.out.println("file " + song.getName() + " in DB is Exist at this moment");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public byte[] findByName(String name, StorageTypes storage_type) throws IOException {
        Source source = Optional.ofNullable(sourceRepository.findByNameAndStorageType(name, storage_type))
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
