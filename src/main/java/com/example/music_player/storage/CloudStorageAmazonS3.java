package com.example.music_player.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.example.music_player.annotation.StorageType;
import com.example.music_player.entity.Source;
import io.minio.GetObjectArgs;
import io.minio.errors.MinioException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
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

@Service
@StorageType(StorageTypes.CLOUD_STORAGE)
public class CloudStorageAmazonS3 implements IStorageSourceService {

    //  @Value("${path.cloud.storage}") //TODO may be do not do this
    @Value("${cloud.AmazonS3.credentials.path-cloud-storage}")
    private String pathCloudStorage;
    @Value("${cloud.AmazonS3.credentials.bucket.name}")
    private String bucketName;

    private final AmazonS3 s3Client;

    @Autowired
    public CloudStorageAmazonS3(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public Source save(MultipartFile multipartFile, Long songId) {
        Source source = null;

        try {
            File fileObject = convertMultipartFileToFile(multipartFile);

            String fileName = multipartFile.getOriginalFilename();
            Resource resource = multipartFile.getResource();
            String contentType = multipartFile.getContentType();

            s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObject));
            fileObject.delete();

            source = new Source(fileName
                    , pathCloudStorage
                    , resource.contentLength()
                    , DigestUtils.md5Hex(resource.getInputStream())//TODO to not read input stream twice
                    , contentType);

            source.setStorage_types(StorageTypes.FILE_SYSTEM);
            source.setStorage_id(1L);
            source.setSong_id(songId);

        } catch (IOException e) {
            System.out.println("exception in public Source save()" + e.getMessage());
        }
        return source;
    }

    private File convertMultipartFileToFile(MultipartFile multipartFile) {
        File convertedFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try {
            FileOutputStream fos = new FileOutputStream(convertedFile);
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            System.out.println("Exeption IN : convertMultipartFileToFile()" + e.getMessage());
        }
        return convertedFile;
    }

    @Override
    public InputStream findSongBySource(Source source) throws IOException {
        S3Object s3Object = s3Client.getObject(bucketName, source.getName());
        S3ObjectInputStream s3InputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(s3InputStream);
            return new ByteArrayResource(content).getInputStream();

        } catch (IOException e) {
            System.out.println("Exeption IN : downloadFile()" + e.getMessage());
        }
        return null;
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


    @Override
    public Source saveZip(Resource resource, String name, String contentType) {
        return null;
    }
}
