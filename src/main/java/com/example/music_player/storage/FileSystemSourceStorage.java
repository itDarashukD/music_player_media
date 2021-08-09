package com.example.music_player.storage;

import com.example.music_player.entity.Source;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
public class FileSystemSourceStorage implements IStorageSourceService {

    @Value("${path.local.storage}")
    private String pathLocalStorage;

    @SneakyThrows
    @Override
    public List<Source> save(InputStream inputStream, String originalFilename, String contentType) {
        final ByteArrayOutputStream byteArray = copingInputStreamToArray(inputStream);
 //       final String checksum = getChecksum(inputStream);

        Path path = Paths.get(pathLocalStorage, originalFilename);
        if (!Files.exists(path)) {
            log.info("IN FileSystemSourceStorage save() : file not exist as yet");
            try {
                log.info("IN FileSystemSourceStorage save() : coping file begin...");
               Files.copy(getCloneInputStream(byteArray)
//                Files.copy(inputStream
                        , path
                        , StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                log.error("ERROR IN FileSystemSourceStorage save() :" + e.getMessage());
            }
        } else
            log.info("File is exist now in this directory : " + pathLocalStorage + originalFilename);
        return createSource(originalFilename, contentType, path, getCloneInputStream(byteArray));
    }


    private ByteArrayOutputStream copingInputStreamToArray(InputStream inputStream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            inputStream.transferTo(baos);
        } catch (IOException e) {
            log.info("IN FileSystemSourceStorage copingInputStreamToArray() : coping inputstram to array...");
        }
        return baos;
    }

    private InputStream getCloneInputStream(ByteArrayOutputStream baos) {
        log.info(" IN FileSystemSourceStorage getCloneInputStream() : cloning begin");
        return new ByteArrayInputStream(baos.toByteArray());
    }

    @SneakyThrows
    private List<Source> createSource(String originalFilename, String contentType, Path path,InputStream inputStream) {
        Source source = new Source(originalFilename
                , pathLocalStorage
                , path.toFile().length()
                , DigestUtils.md5Hex(inputStream)
                //               , checksum
                , contentType);

        source.setStorage_types("FILE_SYSTEM");
        source.setStorage_id(1L);
        return Collections.singletonList(source);
    }

    @Override
    public InputStream findSongBySource(Source source) throws IOException {
        Path path = Paths.get(source.getPath(), source.getName());
        InputStream inputStream = new FileSystemResource(path).getInputStream();
        return inputStream;
    }

    @Override
    public boolean isExist(Source source) {
        log.info("FileSystemSourceStorage isExist : method begining");

        String sourceFilePath = source.getPath();
        String sourceFileName = source.getName();
        final Path path = Paths.get(sourceFilePath, sourceFileName);
        return Files.exists(path);
    }

    @Override
    public void delete(Source source) {
        String sourceFilename = source.getName();
        String sourceFilePath = source.getPath();
        Path path = Paths.get(sourceFilePath, sourceFilename);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            log.error("IN FileSystemSourceStorage delete() :" + e.getMessage());
        }
    }

    @Override
    public String getTypeStorage() {
        return "FILE_SYSTEM";
    }
}



