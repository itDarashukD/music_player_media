package com.example.music_player.storage;

import com.example.music_player.xexperimentDirectory.annotation.StorageType;
import com.example.music_player.entity.Source;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@StorageType(StorageTypes.FILE_SYSTEM)
public class FileSystemSourceStorage implements IStorageSourceService {

    @Value("${path.local.storage}")
    private String pathLocalStorage;
    private Path path;

    @Override
    public List<Source> save(InputStream inputStream, String originalFilename, String contentType) {
        path = Paths.get(pathLocalStorage, originalFilename);
        if (!Files.exists(path)) {
            try {
                Files.copy(inputStream
                        , path
                        , StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                log.error("IN FileSystemSourceStorage save() :" + e.getMessage());
            }
        } else log.info("File is exist now in this directory!" + pathLocalStorage + "/" + originalFilename);
        return createSource(originalFilename, contentType);
    }

    private List<Source> createSource(String originalFilename, String contentType) {
        File file = new File(String.valueOf(path));
        Source source = new Source(originalFilename
                , pathLocalStorage
                , file.length()
                , DigestUtils.md5Hex(originalFilename)
                , contentType);

        source.setStorage_types(StorageTypes.FILE_SYSTEM);
        source.setStorage_id(1L);
        return Collections.singletonList(source);
    }

    @Override
    public InputStream findSongBySource(Source source) throws IOException {
        Path path = Paths.get(source.getPath(), source.getName());
        return new FileSystemResource(path).getInputStream();
    }

    @Override
    public boolean isExist(Source source) {
        String sourceFilePath = source.getPath();
        String sourceFileName = source.getName();
        return Files.exists(Paths.get(sourceFilePath, sourceFileName));
    }

    @Override
    public void delete(Source source) {
        String sourceFilename = source.getName();
        String sourceFilePath = source.getPath();
        try {
            Files.delete(Paths.get(sourceFilePath, sourceFilename));
        } catch (IOException e) {
            log.error("IN FileSystemSourceStorage delete() :" + e.getMessage());
        }
    }
}



