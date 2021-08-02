package com.example.music_player.storage;

import com.example.music_player.entity.Source;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StorageRouterTest {

    private Source source1;
    private InputStream inputStream;
    private Map<String, IStorageSourceService> storagesMap;
    private List<IStorageSourceService> storageSourceList;
    private StorageRouter storageRouter;//Why I can't @InjectMock here and can't Mock the storageSourceList - to injection

    @Mock
    List<Source> mockSourceList;
    @Mock
    CloudStorageAmazonS3 cloudStorageAmazonS3;

    @BeforeEach
    public void beforeEachMethod() {
        source1 = new Source(
                1L
                , 1L
                , 1L
                , "testName"
                , "/PATH_TEST_FILES/testFile"
                , 31000L
                , "checksum"
                , "CLOUD_STORAGE"
                , "audio/mpeg");

        inputStream = new ByteArrayInputStream(StandardCharsets.UTF_16.encode("testString").array());
        storagesMap = Map.ofEntries(Map.entry("CLOUD_STORAGE", cloudStorageAmazonS3));
        storageSourceList = Arrays.asList(cloudStorageAmazonS3);
        storageRouter = new StorageRouter(storageSourceList);
        ReflectionTestUtils.setField(storageRouter, "storagesMap", storagesMap);
    }

    @Test
    void save() {
        when(cloudStorageAmazonS3
                .save(any(InputStream.class), eq("testName"), eq("audio/mpeg")))
                .thenReturn(mockSourceList);
        when(mockSourceList.get(0)).thenReturn(source1);

        storageRouter.save(inputStream, "testName", "audio/mpeg");

        verify(cloudStorageAmazonS3, times(1))
                .save(any(InputStream.class), eq("testName"), eq("audio/mpeg"));
    }

    @Test
    void delete() {
        storageRouter.delete(source1);
        verify(cloudStorageAmazonS3, times(1)).delete(source1);
    }

    @Test
    void isExist() {
        storageRouter.isExist(source1);
        verify(cloudStorageAmazonS3, times(1)).isExist(source1);
    }

    @Test
    void findSongBySource() throws IOException {
        when(cloudStorageAmazonS3.findSongBySource(source1)).thenReturn(inputStream);
        storageRouter.findSongBySource(source1);
        verify(cloudStorageAmazonS3, times(1)).findSongBySource(source1);
    }

    @Test
    void getTypeStorage() {
        assertEquals(storageRouter.getTypeStorage(), "STORAGE_ROUTER");
    }
}