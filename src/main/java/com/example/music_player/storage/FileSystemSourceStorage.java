package com.example.music_player.storage;

import com.example.music_player.annotation.StorageType;
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
//    @Override //TODO maybe this method is redundant
//    public Source saveZip(Resource resource, String originalFilename, String contentType) {
//        Source source = null;
//
//        if (!Files.exists(Paths.get(pathLocalStorage, originalFilename))) {
//            try {
//                Files.copy(resource.getInputStream()
//                        , Paths.get(pathLocalStorage, originalFilename)
//                        , StandardCopyOption.REPLACE_EXISTING);
//
//                source = new Source(originalFilename
//                        , pathLocalStorage
//                        , resource.contentLength()
//                        , DigestUtils.md5Hex(resource.getInputStream())
//                        , contentType);
//
//                source.setStorage_types(StorageTypes.FILE_SYSTEM);
//                source.setStorage_id(1L);
//                source.setSong_id(songIdCount);
//
//            } catch (IOException e) {
//                System.out.println("exception in public Source save()" + e.getMessage());
//            }
//        } else System.out.println("File is exist now in this directory!" + pathLocalStorage + "/" + originalFilename);
//        return source;
//    }
    //inputStream save
    //    @Override
//    public void save(InputStream inputStream,Path path) {
//
//       String filePath = pathLocalStorage + "/" + originalFilename;
//        Path filepath = Paths.get(dir.toString(), inputStream.getOriginalFilename());
//        Path root = Paths.get(pathLocalStorage);
//
//        try {
//            OutputStream os = Files.newOutputStream(path);
//            os.write(inputStream.read());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    //this work by byte[]
    //       public ResponseEntity<byte[]> findSongBySource(Source source) {
//        byte[] targetFile = new byte[0];
//
//        String sourceFilePath = source.getPath();
//        String sourceFileName = source.getName();
//        File file = new File(sourceFilePath + "/" + sourceFileName);
//
//        try {
//            targetFile = FileUtils.readFileToByteArray(file);
//        } catch (IOException e) {
//            System.out.println("Exception in <byte[]> findById(Long id) " + e.getMessage());
//        }
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .contentLength(targetFile.length)
//                .header("targetFile", "application/octet-stream")
//                .header("targetFile-disposition", "attachment; filename=\"" + source.getName() + "\"")
//                .body(targetFile);
//    }
}



