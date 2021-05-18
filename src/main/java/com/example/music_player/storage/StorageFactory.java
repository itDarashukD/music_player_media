package com.example.music_player.storage;


import com.example.music_player.entity.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static java.lang.System.out;

@Component
public class StorageFactory {

    @Autowired
    private final Collection<IStorageSourceService> storagesList;

    public StorageFactory(Collection<IStorageSourceService> storagesList) {
        this.storagesList = storagesList;
    }

    public void prepareStorageForStorageMap() {
        for (IStorageSourceService storage : storagesList
        ) {
            if (storage instanceof FileSystemSourceStorage) {
                putStorage(StorageTypes.FILE_SYSTEM, storage);
            } else if (storage instanceof CloudStorageAmazonS3) {
                putStorage(StorageTypes.CLOUD_STORAGE, storage);
            }
        }
    }

    private final Map<StorageTypes, IStorageSourceService> storagesMap = new HashMap<>();

    public void putStorage(StorageTypes types, IStorageSourceService storageSourceService) {
        storagesMap.put(types, storageSourceService);
    }

    public Source save(InputStream inputStream, String filename, String contentType, StorageTypes storageType) {
        prepareStorageForStorageMap();

        //if we want to save file into both storage:
        for(Entry<StorageTypes,IStorageSourceService> var : storagesMap.entrySet()) {
            return var.getValue().save(inputStream, filename, contentType);
        }

        //if we want to choose storage in who will be save file
        if (storageType.equals(StorageTypes.FILE_SYSTEM)) {
            return storagesMap.get(storageType).save(inputStream, filename, contentType);
        } else if (storageType.equals(StorageTypes.CLOUD_STORAGE)) {
            return storagesMap.get(storageType).save(inputStream, contentType, contentType);
        }
        return null;
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
}
