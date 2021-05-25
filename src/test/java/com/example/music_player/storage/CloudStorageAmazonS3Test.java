package com.example.music_player.storage;

import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = "application-test.properties")
public class CloudStorageAmazonS3Test {

    private static final String KEY_NAME = "TestFile.mp3";
    private TransferManager tm;
    private ProgressListener progressListener;
    private final String bucketName = "music-player-bucked";

    @Autowired
    private AmazonS3 s3Client;

//    @Autowired
//    private MockMvc mockMvc;

    @Before
    public void setup() {
        s3Client = mock(AmazonS3.class);
        tm = TransferManagerBuilder
                .standard()
                .withS3Client(s3Client)
                .withMultipartUploadThreshold((long) (5 * 1024 * 1025))
                .withExecutorFactory(() -> Executors.newFixedThreadPool(5))
                .build();
        progressListener =
                progressEvent -> System.out.println("Transferred bytes: " + progressEvent.getBytesTransferred());
    }

    @Test
    public void save() {
        File file = mock(File.class);
        PutObjectResult s3Result = mock(PutObjectResult.class);
        Mockito.when(s3Client.putObject(new PutObjectRequest(bucketName, file.getName(), file))).thenReturn(s3Result);

        when(file.getName()).thenReturn(KEY_NAME);
        PutObjectRequest request = new PutObjectRequest(bucketName, file.getName(), file);
        request.setGeneralProgressListener(progressListener);
        Upload upload = tm.upload(request);
        assertThat(upload).isNotNull();
        //   verify(s3Client).putObject(request);

//        File file = mock(File.class);
//        PutObjectResult result = mock(PutObjectResult.class);
//        when(s3Client.putObject(anyString(), anyString(), (File) any())).thenReturn(result);
//
//        assertThat(s3Client.putObject(bucketName, KEY_NAME, file)).isEqualTo(result);
//        verify(s3Client).putObject(bucketName, KEY_NAME, file);
    }

    @Test
    public void findSongBySource() {
        S3Object s3Object = mock(S3Object.class);
        when(s3Client.getObject(anyString(), anyString())).thenReturn(s3Object);

        assertThat(s3Client.getObject(bucketName, KEY_NAME).equals(s3Object));
        verify(s3Client).getObject(bucketName, KEY_NAME);
//        S3Object s3Object = mock(S3Object.class);
//        Mockito.when(s3Client.getObject(bucketName, KEY_NAME)).thenReturn(s3Object);
//        this.mockMvc.perform(MockMvcRequestBuilders.get("/file/name"))
//
//                .andDo(print())
//                .andExpect(status().isOk());
        //.andExpect(s3Object.getObjectContent(),)
    }

    @Test
    public void delete() {
        DeleteObjectsRequest request = mock(DeleteObjectsRequest.class);
        DeleteObjectsResult result = mock(DeleteObjectsResult.class);
        when(s3Client.deleteObjects(any())).thenReturn(result);
        assertThat(s3Client.deleteObjects(request)).isEqualTo(result);
    }

    @Test
    public void isExist() {
        S3Object s3Object = mock(S3Object.class);
        when(s3Client.getObject(anyString(), anyString())).thenReturn(s3Object);
        assertThat(s3Client.getObject(bucketName, KEY_NAME).equals(s3Object));
        verify(s3Client).getObject(bucketName, KEY_NAME);
    }
}
