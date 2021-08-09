package com.example.music_player.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.CountingInputStream;
import com.example.music_player.entity.Source;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class CloudStorageAmazonS3 implements IStorageSourceService {

    @Value("${cloud.AmazonS3.credentials.path-cloud-storage}")
    private String pathCloudStorage;
    @Value("${cloud.AmazonS3.credentials.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    @SneakyThrows
    @Override
    public List<Source> save(InputStream inputStream, String originalFilename, String contentType) {
        List<Source> sourceList = null;
        DigestInputStream digestIS = getDigestIS(inputStream);
        File tempFile = putInputStreamToFile(digestIS);

        if (tempFile == null) {
            log.error("ERROR IN save() : We got a problem : tempFile == null !");
        }
        try {
            final String checksum = getChecksum(digestIS);
            if (!s3Client.doesBucketExistV2(bucketName)) {
                log.info("IN save() :Bucket " + bucketName + " start creating.");
                s3Client.createBucket(bucketName);
            } else {
                log.info("IN save() :Bucket " + bucketName + "  already exists.");
            }
            s3Client.putObject(new PutObjectRequest(bucketName, originalFilename, tempFile));
            sourceList = createSource(originalFilename, contentType, tempFile,checksum );
        } catch (Exception e) {
            log.error("ERROR IN save() : We got a problem :" + e.getMessage());
        } finally {
            deletedTempFile(tempFile);
        }
        return sourceList;
    }

    private DigestInputStream getDigestIS(InputStream inputStream) throws NoSuchAlgorithmException, IOException {
        log.info("IN getDigestIS save() : creating DigestInputStream...");
        MessageDigest md = MessageDigest.getInstance("MD5");
        DigestInputStream dis = new DigestInputStream(inputStream, md);
        return dis;
    }

    private String getChecksum(DigestInputStream digestIS) {
        log.info("IN getChecksum save() : counting checksum file (DigestInputStream)...");
        StringBuilder checksumSb = new StringBuilder();
        final byte[] digestMD5 = digestIS.getMessageDigest().digest();
        for (byte digestByte : digestMD5) {
            checksumSb.append(String.format("%02x", digestByte));
        }
        return checksumSb.toString();
    }

    private void deletedTempFile(File tempFile) {
        try {
            Files.deleteIfExists(tempFile.toPath());
            log.info("IN CloudStorageAmazonS3 save() : " + tempFile.getName() + " was deleted");
        } catch (IOException e) {
            log.error("IN CloudStorageAmazonS3 deletedTempFile() : "
                    + tempFile.getName() + " not deleted because : " + e.getMessage());
        }
    }

    @SneakyThrows
    private List<Source> createSource(String originalFilename, String contentType, File tempFile, String checksum) {
        Source source = new Source(originalFilename
                , pathCloudStorage
                , tempFile.length()
                , checksum
                , contentType);
        source.setStorage_types("CLOUD_STORAGE");
        source.setStorage_id(2L);
        return Collections.singletonList(source);
    }

    private File putInputStreamToFile(DigestInputStream digestInputStream) {
        File tempFile;
        try {
            tempFile = File.createTempFile("Epam_MusicPlayer-", ".tmp");
            FileUtils.copyInputStreamToFile(digestInputStream, tempFile);

            log.info("IN putInputStreamToFile() : " + tempFile.getName() + " was created ,bytes length = " + tempFile.length());
        } catch (IOException e) {
            log.error("ERROR IN putInputStreamToFile() :" + e.getMessage());
            throw new UncheckedIOException(" Can't create a tempFile or write inputStream to him !" + e.getMessage()
                    , new IOException());
        }
        return tempFile;
    }

    @Override
    public InputStream findSongBySource(Source source) {
        S3Object s3Object = s3Client.getObject(bucketName, source.getName());
        return s3Object.getObjectContent();
    }

    @Override
    public void delete(Source source) {
        s3Client.deleteObject(bucketName, source.getName());
        log.info("file : " + source.getName() + " is successfully deleted from bucket " + bucketName);
    }

    @Override
    public boolean isExist(Source source) {
        log.info("IN isExist() : method started");
        return s3Client.doesObjectExist(bucketName, source.getName());
    }

    @Override
    public String getTypeStorage() {
        return "CLOUD_STORAGE";
    }
}
