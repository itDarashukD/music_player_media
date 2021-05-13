package com.example.music_player.storage;

import com.example.music_player.entity.Source;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

//@Service
public class CloudStorage implements IStorageSourceService {

  //  @Value("${cloud.MiniO.credentials.backed}")
    @Value("${cloud.AmazonS3.credentials.bucket.name}")
    private String bucked;
    @Value("${cloud.MiniO.credentials.endpoint}")
     private String endpoint;
    //@Value("${cloud.MiniO.credentials.path-cloud-storage}")
    @Value("${cloud.AmazonS3.credentials.path-cloud-storage}")
    private String pathCloudStorage;
    private final MinioClient minioClient;

    @Autowired
    public CloudStorage(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public Source save(MultipartFile multipartFile, Long songId) {
        try {
           minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucked)
                            .object(multipartFile.getOriginalFilename())
                            .filename(convertMultipartFileToFile(multipartFile).getName())
                            .build());

            System.out.println("file "+multipartFile.getName()+" is successfully uploaded as to bucket " + bucked);
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e.getMessage());
            System.out.println("HTTP trace: " + e.httpTrace());
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
        return createSource(multipartFile, songId);
    }

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

            source.setStorage_types(StorageTypes.CLOUD_STORAGE);
            source.setStorage_id(2L);
            source.setSong_id(songId);
        } catch (IOException e) {
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
    public boolean isExist(Source source) {
        Boolean isObjectExist = false;
        try {
            InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucked)
                            .object(source.getName())
                            .build());

            if (stream != null) {
                isObjectExist = true;
            }
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e.getMessage());
            System.out.println("HTTP trace: " + e.httpTrace());
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
        return isObjectExist;
    }

    @Override
    public void delete(Source source) {
        String sourceFilename = source.getName();
        try {
            minioClient.removeObject(
                    RemoveObjectArgs
                            .builder()
                            .bucket(bucked)
                            .object(sourceFilename)
                            .build());

            System.out.println("file : " + sourceFilename + " is successfully deleted from bucket " + bucked);
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e.getMessage());
            System.out.println("HTTP trace: " + e.httpTrace());
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    @Override
    public InputStream findSongBySource(Source source) {
        InputStream stream = null;
        try {
            stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucked)
                            .object(source.getName())
                            .build());

        } catch (MinioException e) {
            System.out.println("Error occurred: " + e.getMessage());
            System.out.println("HTTP trace: " + e.httpTrace());
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
        return stream;
    }

    @Override
    public Source saveZip(Resource resource, String name, String contentType) {
        return new Source();
    }
}
//    InputStream initialStream = multipartFile.getInputStream();
//    byte[] buffer = new byte[initialStream.available()];
//            initialStream.read(buffer);
//            try (OutputStream outStream = new FileOutputStream(convertedFile)) {
//        outStream.write(buffer);
//    }

