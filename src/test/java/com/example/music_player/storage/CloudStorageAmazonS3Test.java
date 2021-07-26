package com.example.music_player.storage;

import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.example.music_player.entity.Source;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CloudStorageAmazonS3Test {

    private String bucketName;
    private AmazonS3 s3Client;
    private File fileObject;
    private File file;
    private File tempFile;
    private InputStream inputStream;
    private Bucket bucket;
    private PutObjectResult putObjectResult;
    private Source source;
    private S3Object s3Object;
    private S3ObjectInputStream s3InputStream;

    @InjectMocks
    CloudStorageAmazonS3 cloudStorageAmazonS3;

    @BeforeEach
    public void setup() {
        bucketName = "music-player-bucked";
        fileObject = mock(File.class);
        file = mock(File.class);
        s3Client = mock(AmazonS3.class);
        bucket = mock(Bucket.class);
        putObjectResult = mock(PutObjectResult.class);
        inputStream = new ByteArrayInputStream(StandardCharsets.UTF_16.encode("testString").array());
        tempFile = mock(File.class);
        source = mock(Source.class);
        s3Object = mock(S3Object.class);
        s3InputStream = mock(S3ObjectInputStream.class);
        cloudStorageAmazonS3 = new CloudStorageAmazonS3();
        ReflectionTestUtils.setField(cloudStorageAmazonS3, "s3Client", s3Client);
        ReflectionTestUtils.setField(cloudStorageAmazonS3, "bucketName", bucketName);
    }

    @Test
    public void saveWhenBucketNotExist() {
        when(s3Client.doesBucketExistV2(bucketName)).thenReturn(false);
        when(s3Client.createBucket(bucketName)).thenReturn(bucket);
        when(s3Client.putObject(any())).thenReturn(putObjectResult);

        cloudStorageAmazonS3.save(inputStream, "originalFilename", "contentType");

        verify(s3Client, times(1)).createBucket(bucketName);
        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class));
        assertThat(fileObject.delete()).isEqualTo(false);
    }

    @Test
    public void saveWhenBucketExistNow() {  //TODO Nullpointer s3Client in CloudStorageAmazonS3, How to properly mock it?
        when(s3Client.doesBucketExistV2(bucketName)).thenReturn(true);
        when(s3Client.putObject(any())).thenReturn(putObjectResult);

        cloudStorageAmazonS3.save(inputStream, "originalFilename", "contentType");

        verify(s3Client, never()).createBucket(bucketName);
        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class));
        assertThat(fileObject.delete()).isEqualTo(false);
    }

    @Test
    public void findSongBySource() {
        when(s3Client.getObject(bucketName, source.getName())).thenReturn(s3Object);
        when(s3Object.getObjectContent()).thenReturn(s3InputStream);

        cloudStorageAmazonS3.findSongBySource(source);

        verify(s3Client, times(1)).getObject(bucketName, source.getName());
        verify(s3Object, times(1)).getObjectContent();
    }

    @Test
    public void delete() {
        cloudStorageAmazonS3.delete(source);
        verify(s3Client, times(1)).deleteObject(bucketName, source.getName());
    }

    @Test
    public void isExistWhensS3ObjectNotNull() {
        when(s3Client.getObject(bucketName, source.getName())).thenReturn(s3Object);
        assertEquals(cloudStorageAmazonS3.isExist(source), true);
        verify(s3Client, times(1)).getObject(bucketName, source.getName());
    }

    @Test
    public void isExistWhensS3ObjectIsNull() {
        when(s3Client.getObject(bucketName, source.getName())).thenReturn(null);
        assertEquals(cloudStorageAmazonS3.isExist(source), false);
        verify(s3Client, times(1)).getObject(bucketName, source.getName());
    }
}
