package com.example.music_player.storage;


import com.example.music_player.entity.Source;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Slf4j
@Component
@Primary
public class StorageRouter implements IStorageSourceService {

    private final Map<String, IStorageSourceService> storagesMap = new HashMap<>();
    private final List<IStorageSourceService> storageSourceList;

    @Autowired
    public StorageRouter(List<IStorageSourceService> storageSourceList) {
        this.storageSourceList = storageSourceList;

        for (IStorageSourceService storage : storageSourceList) {
            storagesMap.put(storage.getTypeStorage(), storage);
        }
    }

    @Override
    public List<Source> save(InputStream inputStream, String filename, String contentType) {
        List<Source> sourceList = new ArrayList<>();
        File fileWithInputStream = putInputStreamToFile(inputStream);

        for (Entry<String, IStorageSourceService> pair : storagesMap.entrySet()) {
            Source source = pair.getValue().save(getInputStreamFromFile(fileWithInputStream), filename, contentType).get(0);
            sourceList.add(source);
        }
        closeInputStream(inputStream);
        deleteTempFile(fileWithInputStream);
        return sourceList;
    }

    private void deleteTempFile(File fileWithInputStream) {
        fileWithInputStream.deleteOnExit();
    }

    private void closeInputStream(InputStream inputStream) {
        try {
            inputStream.close();
        } catch (IOException e) {
            log.error("Exception IN : closeInputStream()" + e.getMessage());
        }
    }

    @Override
    public void delete(Source source) {
        storagesMap.get(source.getStorage_types()).delete(source);
    }

    @Override
    public boolean isExist(Source source) {
        return storagesMap.get(String.valueOf(source.getStorage_types())).isExist(source);
    }

    @Override
    public InputStream findSongBySource(Source source) throws IOException {
        return storagesMap.get(String.valueOf(source.getStorage_types())).findSongBySource(source);
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
            log.error("Exception IN : putInputStreamToFile()" + e.getMessage());
        }
        return InputStreamFromFile;
    }

    @Override
    public String getTypeStorage() {
        return "STORAGE_ROUTER";
    }
}

