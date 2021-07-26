package com.example.music_player.storage;

import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.example.music_player.MusicPlayerApplication;
import com.example.music_player.entity.Source;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

//@SpringBootTest(properties = "application-test.properties", classes = {MusicPlayerApplication.class} )
@ExtendWith(MockitoExtension.class)
public class CloudStorageAmazonS3Test {

    private static final String KEY_NAME = "TestFile.mp3";
    private TransferManager tm;
    private ProgressListener progressListener;
    private String bucketName;
    private Boolean found;
    private AmazonS3 s3Client;

    File fileObject;
    File file;
    File tempFile;
    InputStream inputStream;
    Bucket bucket;
    PutObjectResult putObjectResult;
    Source source;
    S3Object s3Object;
    S3ObjectInputStream s3InputStream;

    @InjectMocks
    CloudStorageAmazonS3 cloudStorageAmazonS3;

    @BeforeEach
    public void setup() {
        bucketName = "music-player-bucked";
        fileObject = mock(File.class);
        file = mock(File.class);
        s3Client = mock(AmazonS3.class);
        inputStream = mock(InputStream.class);
        bucket = mock(Bucket.class);
        putObjectResult = mock(PutObjectResult.class);
        inputStream = new ByteArrayInputStream(StandardCharsets.UTF_16.encode("testString").array());
        tempFile = mock(File.class);
        source = mock(Source.class);
        s3Object = mock(S3Object.class);
        s3InputStream = mock(S3ObjectInputStream.class);
        cloudStorageAmazonS3 = new CloudStorageAmazonS3();
        ReflectionTestUtils.setField(cloudStorageAmazonS3, "s3Client", s3Client);
    }

//    @Autowired
//    private MockMvc mockMvc;

//    @Before
//    public void setup() {
//        s3Client = mock(AmazonS3.class);
//        tm = TransferManagerBuilder
//                .standard()
//                .withS3Client(s3Client)
//                .withMultipartUploadThreshold((long) (5 * 1024 * 1025))
//                .withExecutorFactory(() -> Executors.newFixedThreadPool(5))
//                .build();
//        progressListener =
//                progressEvent -> System.out.println("Transferred bytes: " + progressEvent.getBytesTransferred());
//    }

    @Test
    public void saveWhenBucketNotExist() {  //TODO Nullpointer s3Client in CloudStorageAmazonS3, How to properly mock it?
        when(s3Client.doesBucketExistV2(bucketName)).thenReturn(true);

        when(s3Client.createBucket(bucketName)).thenReturn(bucket);
        when(s3Client.putObject(any())).thenReturn(putObjectResult);

        cloudStorageAmazonS3.save(inputStream, "originalFilename", "contentType"); //TODO NULL pointer

        verify(s3Client, times(1)).createBucket(bucketName);
        verify(s3Client, times(1)).putObject(new PutObjectRequest(bucketName, "originalFilename", fileObject));
        verify(fileObject.delete()).equals(true);
        verify(fileObject, times(1)).delete();
        verify(tempFile, times(1)).deleteOnExit();
    }

    @Test
    public void saveWhenBucketExistNow() {  //TODO Nullpointer s3Client in CloudStorageAmazonS3, How to properly mock it?
        when(s3Client.doesBucketExistV2(bucketName)).thenReturn(false);

        when(s3Client.createBucket(bucketName)).thenReturn(bucket);
        when(s3Client.putObject(new PutObjectRequest(bucketName, "originalFilename", fileObject))).thenReturn(putObjectResult);

        cloudStorageAmazonS3.save(inputStream, "originalFilename", "contentType"); //TODO NULL pointer

        verify(s3Client, times(0)).createBucket(bucketName);
        verify(s3Client, times(1)).putObject(new PutObjectRequest(bucketName, "originalFilename", fileObject));
        verify(fileObject.delete()).equals(true);
        verify(fileObject, times(1)).delete();
        verify(tempFile, times(1)).deleteOnExit();
    }
    //   File file = mock(File.class);
//        PutObjectResult s3Result = mock(PutObjectResult.class);
//        Mockito.when(s3Client.putObject(new PutObjectRequest(bucketName, file.getName(), file))).thenReturn(s3Result);
//
//        when(file.getName()).thenReturn(KEY_NAME);
//        PutObjectRequest request = new PutObjectRequest(bucketName, file.getName(), file);
//        request.setGeneralProgressListener(progressListener);
//        Upload upload = tm.upload(request);
//
//        assertThat(upload).isNotNull();


    @Test
    public void findSongBySource() {
        when(s3Client.getObject(bucketName, source.getName())).thenReturn(s3Object);
        when(s3Object.getObjectContent()).thenReturn(s3InputStream);

        cloudStorageAmazonS3.findSongBySource(source);  //TODO Nullpointer s3Client in CloudStorageAmazonS3, How to properly mock it?

        verify(s3Client, times(1)).getObject(bucketName, source.getName());
        verify(s3Object, times(1)).getObjectContent();
    }

    @Test
    public void delete() {
        doNothing().when(s3Client).deleteObject(bucketName, source.getName());

        cloudStorageAmazonS3.delete(source);  //TODO Nullpointer s3Client in CloudStorageAmazonS3,

        verify(s3Client, times(1)).deleteObject(bucketName, source.getName());


//        DeleteObjectsRequest request = mock(DeleteObjectsRequest.class);
//        DeleteObjectsResult result = mock(DeleteObjectsResult.class);
//        when(s3Client.deleteObjects(any())).thenReturn(result);
//        assertThat(s3Client.deleteObjects(request)).isEqualTo(result);
    }

    @Test
    public void isExist() {
        S3Object s3Object = mock(S3Object.class);
        when(s3Client.getObject(anyString(), anyString())).thenReturn(s3Object);
        assertThat(s3Client.getObject(bucketName, KEY_NAME).equals(s3Object));
        verify(s3Client).getObject(bucketName, KEY_NAME);
    }
}
