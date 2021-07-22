package com.example.music_player.service;

import com.example.music_player.entity.Song;
import com.example.music_player.entity.Source;
import com.example.music_player.repository.ISourceRepository;
import com.example.music_player.storage.IStorageSourceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SourceServiceTest {

    private Source source1;
    private Source source2;
    private String testStorage_type;
    private byte[] testArray;
    private Boolean testBoolean;
    private List<Source> sourceList;
    private InputStream inputStream;

    @Mock
    ISourceRepository sourceRepository;
    @Mock
    IStorageSourceService storageSourceService;
    @Mock
    MultipartFile mockMultipartFile;
    @Mock
    Song song;
    @InjectMocks
    SourceService sourceService;


    @BeforeEach
    public void setup() {
        source1 = new Source(1L, 1L, 1L, "name1", "/test/path", 1111L
                , "checksum", "storageTypes", "fileType");
        source2 = new Source(2L, 2L, 2L, "name2", "/test/path2", 2222L
                , "checksum2", "storageTypes2", "fileType2");

        testStorage_type = "testStorageType";
        testArray = "testByteArray".getBytes();
        testBoolean = false;
        sourceList = List.of(source1, source2);
        inputStream = new ByteArrayInputStream(StandardCharsets.UTF_16.encode("testString").array());
    }

    @Test
    void saveWhenSourceDoNotExist() throws IOException {
        when(sourceRepository.isExistByNameAndFileType(song.getName()
                , mockMultipartFile.getContentType()))
                .thenReturn(testBoolean);
        when(storageSourceService.save(
                mockMultipartFile.getInputStream()
                , mockMultipartFile.getName()
                , mockMultipartFile.getContentType()))
                .thenReturn(sourceList);
        doNothing().when(sourceRepository).save(source1);

        sourceService.save(mockMultipartFile, song, song.getId());
        assertEquals(testBoolean, false);
        verify(storageSourceService, times(1))
                .save(mockMultipartFile.getInputStream()
                        , mockMultipartFile.getName()
                        , mockMultipartFile.getContentType());
        verify(sourceRepository, times(1)).save(source1);
    }

    @Test
    void saveWhenSourceExist() throws IOException {//second branch of save() method
        lenient().when(sourceRepository.isExistByNameAndFileType(song.getName()
                , mockMultipartFile.getContentType()))
                .thenReturn(true);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                        sourceService.save(mockMultipartFile, song, song.getId()),
                "source do not fined");
        String expectedMessage = "do not fined";
        Assertions.assertTrue(exception.getMessage().contains(expectedMessage));

        verify(storageSourceService, times(0)).save(mockMultipartFile.getInputStream()
                , mockMultipartFile.getName()
                , mockMultipartFile.getContentType());
        verify(sourceRepository, times(0)).save(source1);
    }

    @Test
    void findByName() throws IOException { //TODO second branch
        lenient().when(sourceRepository.findByNameAndStorageType(
                source1.getName()
                , source1.getStorage_types()
                , source1.getFileType()))
                .thenReturn(source1);
        when(storageSourceService.findSongBySource(source1)).thenReturn(inputStream);

        sourceService.findByName(source1.getName(), source1.getStorage_types(), source1.getFileType());
        verify(sourceRepository, times(1))
                .findByNameAndStorageType(
                        source1.getName()
                        , source1.getStorage_types()
                        , source1.getFileType());
    }

    @Test
    void isExist() {
        when(sourceRepository.findById(anyLong())).thenReturn(source1);
        when(storageSourceService.isExist(source1)).thenReturn(testBoolean = true);
        assertEquals(sourceService.isExist(source1.getId()), true);
        verify(sourceRepository, times(1)).findById(anyLong());
    }

    @Test
    void delete() {//TODO: second branch
        when(sourceRepository.findAllByName(source2.getName()))
                .thenReturn(sourceList);
        doNothing().when(storageSourceService).delete(sourceList.get(0));
        doNothing().when(sourceRepository).deleteById(sourceList.get(0).getId());
        sourceService.delete(source2.getName());
        verify(storageSourceService, times(1)).delete(sourceList.get(0));
        verify(sourceRepository, times(1)).deleteById(sourceList.get(0).getId());
        verify(sourceRepository, times(1)).findAllByName(source2.getName());
    }
}