package com.example.music_player.service;

import com.example.music_player.entity.Song;
import com.example.music_player.entity.Source;
import com.example.music_player.repository.ISourceRepository;
import com.example.music_player.storage.StorageRouter;
import com.example.music_player.storage.StorageTypes;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class SourceService implements ISourceService {

    //   private final IStorageSourceService storageSourceService;
    private final ISourceRepository sourceRepository;
    private final StorageRouter storageRouter;
//    private final StorageChooser storageChooser;

    @Autowired
    public SourceService(ISourceRepository sourceRepository, StorageRouter storageRouter) {
        //  this.storageSourceService = storageSourceService;
        this.sourceRepository = sourceRepository;
        this.storageRouter = storageRouter;
    }

    @Transactional
    public void save(MultipartFile multipartFile, Song song, Long songIdFromDB) {
        try {
            InputStream inputStream = multipartFile.getInputStream();
            String fileName = multipartFile.getOriginalFilename();
            String contentType = multipartFile.getContentType();
            if (!sourceRepository.isExistByName(song.getName())) {
                List<Source> source = storageRouter.save(inputStream, fileName, contentType);
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
        Source source = Optional.ofNullable(sourceRepository.findByName(name))
                .orElseThrow(() -> new IllegalStateException("source with " + name + " do not fined"));
        source.setStorage_types(storage_type);
        return IOUtils.toByteArray(storageRouter.findSongBySource(source));
    }

    public boolean isExist(Long id) {
        Source source = Optional.ofNullable(sourceRepository.findById(id))
                .orElseThrow(() -> new IllegalStateException("source with " + id + " do not fined"));
        return storageRouter.isExist(source);
    }

    // @Transactional
    public void delete(Long id) {
        Source source = Optional.ofNullable(sourceRepository.findById(id))
                .orElseThrow(() -> new IllegalStateException("source with " + id + " do not fined"));
        storageRouter.delete(source);
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

