package com.example.music_player.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.example.music_player.entity.Source;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CloudStorageAmazonS3Test {

    private String bucketName;
    private AmazonS3 s3Client;
    private File tempFile;
    private File testedFile;
    private InputStream inputStream;
    private Bucket bucket;
    private PutObjectResult putObjectResult;
    private Source source;
    private S3Object s3Object;
    private S3ObjectInputStream s3InputStream;
    private PutObjectRequest putObjectRequest;
    private List<Source> sourceList;


    @InjectMocks
    CloudStorageAmazonS3 cloudStorageAmazonS3;


    @BeforeEach
    public void setup() throws IOException {
        bucketName = "music-player-bucked";
        s3Client = mock(AmazonS3.class);
        bucket = mock(Bucket.class);
        putObjectResult = mock(PutObjectResult.class);
        inputStream = new ByteArrayInputStream(StandardCharsets.UTF_16.encode("testString").array());
        testedFile = mock(File.class);
        source = new Source(1L, 1L, 1L, "name1", "/test/path", 1111L
                , "checksum", "storageTypes", "fileType");
        s3Object = mock(S3Object.class);
        s3InputStream = mock(S3ObjectInputStream.class);
        cloudStorageAmazonS3 = new CloudStorageAmazonS3();
        tempFile = File.createTempFile("Epam_MusicPlayer-", ".tmp");
        sourceList = Collections.singletonList(source);
        FileUtils.copyInputStreamToFile(inputStream, tempFile);
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
    }

    @Test
    public void saveWhenBucketExistNow() {
        when(s3Client.doesBucketExistV2(bucketName)).thenReturn(true);
        when(s3Client.putObject(any())).thenReturn(putObjectResult);

        cloudStorageAmazonS3.save(inputStream, "originalFilename", "contentType");

        verify(s3Client, never()).createBucket(bucketName);
        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class));
    }

    @Test
    public void isTempFileDeletedWhenThrowException() throws IOException {

        try (MockedStatic<File> utilities = Mockito.mockStatic(File.class)) {
            utilities.when(() -> File.createTempFile(anyString(), anyString())).thenReturn(testedFile);
            assertThat(File.createTempFile("Epam_MusicPlayer-", ".tmp")).isEqualTo(testedFile);
            assertEquals(testedFile.length(), 0);
        }

        when(s3Client.putObject(any())).thenThrow(new RuntimeException());

        try (MockedStatic<Collections> utilities2 = Mockito.mockStatic(Collections.class)) {
            utilities2.when(() -> Collections.singletonList(any(Source.class)))
                    .thenReturn(sourceList);
        }
//        assertThat(cloudStorageAmazonS3.save(inputStream, "originalFilename", "contentType")
//                .get(0)
//                .getStorage_types())
//                .isEqualTo("CLOUD_STORAGE");
       cloudStorageAmazonS3.save(inputStream, "originalFilename", "contentType");
        assertThat(testedFile.exists()).isFalse();
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
