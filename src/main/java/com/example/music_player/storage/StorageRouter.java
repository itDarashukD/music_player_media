package com.example.music_player.storage;


import com.example.music_player.entity.Source;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

@Slf4j
@Component
@Primary
public class StorageRouter implements IStorageSourceService {

//        private final Collection<IStorageSourceService> storagesList;
//    private final Map<StorageTypes, IStorageSourceService> storagesMap = new HashMap<>();
    private final Map<String, IStorageSourceService> storagesMap = new HashMap<>();
    private final List<IStorageSourceService> storageSourceList;

    @Autowired
    public StorageRouter(List<IStorageSourceService> storageSourceList) {
        this.storageSourceList = storageSourceList;

        for (IStorageSourceService storage : storageSourceList
        ) {
            storagesMap.put(storage.getTypeStorage(), storage);
        }
    }

//        @Autowired
//    public StorageRouter(Collection<IStorageSourceService> storagesList) {
//
//        this.storagesList = storagesList;
//        for (IStorageSourceService storage : storagesList
//        ) {
//            if (storage instanceof FileSystemSourceStorage) {//TODO read about the proxy/sedjeylib
//                storagesMap.put(StorageTypes.FILE_SYSTEM, storage);
//            } else if (storage instanceof CloudStorageAmazonS3) {
//                storagesMap.put(StorageTypes.CLOUD_STORAGE, storage);
//            }
//        }
//    }

    @Override
    public List<Source> save(InputStream inputStream, String filename, String contentType) {
        List<Source> sourceList = new ArrayList<>();
        File fileWithInputStream = putInputStreamToFile(inputStream);

        for (Entry<String  , IStorageSourceService> pair : storagesMap.entrySet()) {
            Source source = pair.getValue().save(getInputStreamFromFile(fileWithInputStream), filename, contentType).get(0);
            sourceList.add(source);
        }

//        for (Entry<StorageTypes, IStorageSourceService> pair : storagesMap.entrySet()) {
//            Source source = pair.getValue().save(getInputStreamFromFile(fileWithInputStream), filename, contentType).get(0);
//            sourceList.add(source);
//        }
        return sourceList;
    }

    @Override
    public void delete(Source source) {
        storagesMap.get(source.getStorage_types()).delete(source);
    }

    @Override
    public boolean isExist(Source source) {
        return storagesMap.get(source.getStorage_types()).isExist(source);
    }

    @Override
    public InputStream findSongBySource(Source source) throws IOException {
        return storagesMap.get(source.getStorage_types()).findSongBySource(source);
    }

    private File putInputStreamToFile(InputStream inputStream) {
        File tempFile = null;
        try {
            tempFile = File.createTempFile("Epam_MusicPlayer-Inputstream", ".tmp");
            FileUtils.copyInputStreamToFile(inputStream, tempFile);
        } catch (IOException e) {
            log.error("Exception IN : putInputStreamToFile()" + e.getMessage());
        }
        return tempFile;
    }

    private InputStream getInputStreamFromFile(File putInputStreamToFile) {
        InputStream InputStreamFromFile = null;
        try {
            InputStreamFromFile = new FileInputStream(putInputStreamToFile);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return InputStreamFromFile;
    }

    @Override
    public String getTypeStorage() {
        return "StorageRouter";
    }
}

//if we want to choose storage in which will be save file
//        if (storageType.equals(StorageTypes.FILE_SYSTEM)) {
//            return storagesMap.get(storageType).save(inputStream, filename, contentType);
//        } else if (storageType.equals(StorageTypes.CLOUD_STORAGE)) {
//            return storagesMap.get(storageType).save(inputStream, contentType, contentType);
//        }
