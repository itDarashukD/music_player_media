package com.example.music_player.storage;

import com.example.music_player.config.MinIOConfig;
import com.example.music_player.entity.Source;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class CloudMinIOStorage implements IStorageSourceService {

    @Value("${cloud.MiniO.credentials.backed}")
    private String bucked;
    @Value("${cloud.MiniO.credentials.endpoint}")
    private String endpoint;
    @Value("${cloud.MiniO.credentials.path-cloud-storage}")
    private String pathCloudStorage;
    private MinIOConfig minIOConfig;

    @Autowired
    public CloudMinIOStorage(MinIOConfig minIOConfig) {
        this.minIOConfig = minIOConfig;
    }

    @Override
    public Source save(MultipartFile multipartFile, Long songId) {
        try {
            minIOConfig.minioClient().uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucked)
                            .object(multipartFile.getOriginalFilename())
                            .filename(convertMultipartFileToFile(multipartFile).getName())
                            .build());

            System.out.println("file is successfully uploaded as to bucket " + bucked);
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e.getMessage());
            System.out.println("HTTP trace: " + e.httpTrace());
        } catch (IOException|NoSuchAlgorithmException | InvalidKeyException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
        return createSource(multipartFile, songId);
    }//TODO I can do this with UUID

    private Source createSource(MultipartFile multipartFile, Long songId) {
        Source source = null;
        Resource resource = multipartFile.getResource();
        String originalFilename = multipartFile.getOriginalFilename();
        String contentType = multipartFile.getContentType();
            try {
                source = new Source(originalFilename
                        , pathCloudStorage
                        , resource.contentLength()
                        , DigestUtils.md5Hex(resource.getInputStream())//TODO I can do this with UUID
                        , contentType);

                source.setStorage_types(StorageTypes.AMAZON_S3);
                source.setStorage_id(2L);
                source.setSong_id(songId);//TODO song_Id how autoIncrement or it must be equal to id in Song
            }catch (IOException e){
                System.out.println("Error occurred: " + e.getMessage());
            }

        return source;
    }

    private File convertMultipartFileToFile(MultipartFile multipartFile) {
        File convertedFile = new File((Objects.requireNonNull(multipartFile.getName())));
        try {
            FileOutputStream fos = new FileOutputStream(convertedFile);
            fos.write(multipartFile.getBytes());
            fos.close();
         } catch (IOException e) {
            System.out.println("Exception IN : convertMultipartFileToFile()" + e.getMessage());
        }
        return convertedFile;
    }


    @Override
    public Source saveZip(Resource resource, String name, String contentType) {
        return null;
    }


    @Override
    public Stream<Path> loadAll() {
        return null;
    }

    @Override
    public void delete(Source source) {

    }

    @Override
    public boolean isExist(Source source) {
        return false;
    }

    @Override
    public InputStream findSongBySource(Source source) throws IOException {
        return null;
    }
//    InputStream initialStream = multipartFile.getInputStream();
//    byte[] buffer = new byte[initialStream.available()];
//            initialStream.read(buffer);
//            try (OutputStream outStream = new FileOutputStream(convertedFile)) {
//        outStream.write(buffer);
//    }
}
