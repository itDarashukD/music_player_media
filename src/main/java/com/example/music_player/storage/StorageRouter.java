package com.example.music_player.storage;


import com.example.music_player.entity.Source;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

@Component
public class StorageRouter {


    private final Collection<IStorageSourceService> storagesList;
    private final Map<StorageTypes, IStorageSourceService> storagesMap = new HashMap<>();

    @Autowired
    public StorageRouter(Collection<IStorageSourceService> storagesList) {
        this.storagesList = storagesList;
        for (IStorageSourceService storage : storagesList
        ) {
            if (storage instanceof FileSystemSourceStorage) {//TODO read about the proxy/sedjeylib
                storagesMap.put(StorageTypes.FILE_SYSTEM, storage);
            } else if (storage instanceof CloudStorageAmazonS3) {
                storagesMap.put(StorageTypes.CLOUD_STORAGE, storage);
            }
        }
    }

    public List<Source> save(InputStream inputStream, String filename, String contentType) {
        List<Source> sourceList = new ArrayList<>();
        File fileWithInputStream = putInputStreamToFile(inputStream);

        for (Entry<StorageTypes, IStorageSourceService> pair : storagesMap.entrySet()) {
            Source source = pair.getValue().save(getInputStreamFromFile(fileWithInputStream), filename, contentType);
            sourceList.add(source);
        }
        return sourceList;
    }

    public void delete(Source source) {
        storagesMap.get(source.getStorage_types()).delete(source);
    }

    public boolean isExist(Source source) {
        return storagesMap.get(source.getStorage_types()).isExist(source);
    }

    public InputStream findSongBySource(Source source) throws IOException {
        return storagesMap.get(source.getStorage_types()).findSongBySource(source);
    }

    private File putInputStreamToFile(InputStream inputStream) {
        File tempFile = null;
        try {
            tempFile = File.createTempFile("Epam_MusicPlayer-Inputstream", ".tmp");
            FileUtils.copyInputStreamToFile(inputStream, tempFile);
        } catch (IOException e) {
            System.out.println("Exception IN : putInputStreamToFile()" + e.getMessage());
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
}

//if we want to choose storage in who will be save file
//        if (storageType.equals(StorageTypes.FILE_SYSTEM)) {
//            return storagesMap.get(storageType).save(inputStream, filename, contentType);
//        } else if (storageType.equals(StorageTypes.CLOUD_STORAGE)) {
//            return storagesMap.get(storageType).save(inputStream, contentType, contentType);
//        }
