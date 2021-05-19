package com.example.music_player.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.example.music_player.annotation.StorageType;
import com.example.music_player.entity.Source;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service

@StorageType(StorageTypes.CLOUD_STORAGE)
public class CloudStorageAmazonS3 implements IStorageSourceService {

    @Value("${cloud.AmazonS3.credentials.path-cloud-storage}")
    private String pathCloudStorage;
    @Value("${cloud.AmazonS3.credentials.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;
    private File tempFile;
    private File fileObject;

    public Source save(InputStream inputStream, String originalFilename, String contentType) {
        fileObject = putInputStreamToFile(inputStream);
        s3Client.putObject(new PutObjectRequest(bucketName, originalFilename, fileObject));
        return createSource(originalFilename, contentType);
    }

    private Source createSource(String originalFilename, String contentType) {
        Source source = new Source(originalFilename
                , pathCloudStorage
                , fileObject.length()
                , DigestUtils.md5Hex(originalFilename)//TODO to not read input stream twice
                , contentType);

        source.setStorage_types(StorageTypes.CLOUD_STORAGE);
        source.setStorage_id(2L);
        fileObject.delete();
        tempFile.deleteOnExit();
        return source;
    }

    private File putInputStreamToFile(InputStream inputStream) {
        try {
            tempFile = File.createTempFile("Epam_MusicPlayer-", ".tmp");
            FileUtils.copyInputStreamToFile(inputStream, tempFile);
        } catch (IOException e) {
            System.out.println("Exeption IN : convertMultipartFileToFile()" + e.getMessage());
        }
        return tempFile;
    }

    @Override
    public InputStream findSongBySource(Source source) {
        byte[] content;
        S3Object s3Object = s3Client.getObject(bucketName, source.getName());
        S3ObjectInputStream s3InputStream = s3Object.getObjectContent();
        try {
            content = IOUtils.toByteArray(s3InputStream);
            return new ByteArrayResource(content).getInputStream();

        } catch (IOException e) {
            System.out.println("Exeption IN : downloadFile()" + e.getMessage());
        }
        return InputStream.nullInputStream();
    }

    @Override
    public void delete(Source source) {
        s3Client.deleteObject(bucketName, source.getName());
        System.out.println("file : " + source.getName() + " is successfully deleted from bucket " + bucketName);
    }

    @Override
    public boolean isExist(Source source) {
        Boolean isObjectExist = false;
        S3Object s3Object = s3Client.getObject(bucketName, source.getName());
        S3ObjectInputStream s3InputStream = s3Object.getObjectContent();
        if (s3InputStream != null) {
            isObjectExist = true;
        }
        return isObjectExist;
    }
//
//
//    @Override
//    public Source saveZip(Resource resource, String name, String contentType) {
//        return null;
//    }

//        File convertedFile = new File(Objects.requireNonNull(inputStream.getOriginalFilename()));
//        try {
//            FileOutputStream fos = new FileOutputStream(convertedFile);
//            fos.write(multipartFile.getBytes());
//        } catch (IOException e) {
}
