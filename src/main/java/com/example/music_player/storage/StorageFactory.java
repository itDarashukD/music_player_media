package com.example.music_player.storage;


import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Component
public class StorageFactory {
//this code will be working with @Annotation
    private Map<StorageTypes, IStorageSourceService> storageFactoryMap = new HashMap<>();

    public void getStorage(StorageTypes types, IStorageSourceService storageSourceService) {
        storageFactoryMap.put(types, storageSourceService);
    }

    public void save(MultipartFile multipartFile, Long songId) throws Exception {   //TODO amazon
        if (multipartFile.getContentType().equals("mp3") || multipartFile.getContentType().equals("wav")) {
            storageFactoryMap.get(StorageTypes.valueOf("FILE_SYSTEM")).save(multipartFile,songId);
        }if (multipartFile.getContentType().equals("zip")){
            storageFactoryMap.get(StorageTypes.valueOf("AMAZON_S3")).saveZip(multipartFile.getResource()
            , multipartFile.getOriginalFilename()
            , multipartFile.getContentType());
        }
    }
}
//    private FileSystemSourceStorage fileSystemSourceStorage;
//    private CloudSourceStorage cloudSourceStorage;
//    @Autowired
//    public StorageFactory(FileSystemSourceStorage fileSystemSourceStorage, CloudSourceStorage cloudSourceStorage) {
//        this.fileSystemSourceStorage = fileSystemSourceStorage;
//        this.cloudSourceStorage = cloudSourceStorage;
//    }
//
//    public void save(Resource resource, String name, String contentType) throws Exception {   //TODO amazon
//        if (contentType.equals("mp3") || contentType.equals("wav")) {
//            fileSystemSourceStorage .save(resource, name, contentType);
//
//        }if (contentType.equals("zip")){
//            cloudSourceStorage.save(resource, name, contentType);
//        }