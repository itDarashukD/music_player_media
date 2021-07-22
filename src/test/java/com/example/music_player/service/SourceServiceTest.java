package com.example.music_player.service;

import com.amazonaws.util.IOUtils;
import com.example.music_player.entity.Source;
import com.example.music_player.repository.ISourceRepository;
import com.example.music_player.storage.IStorageSourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SourceServiceTest {

    private Source source1;
    private Source source2;
    private String testStorage_type;
    private byte[] testArray;
    private Boolean testBoolean;
    private List<Source> sourceList;

    @Mock
    ISourceRepository sourceRepository;
    @Mock
    IStorageSourceService storageSourceService;
    @Mock
    InputStream inputStream;

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
    }

    @Test
    void save() {

    }

    @Test
    void findByName() {
        when(sourceRepository.findByNameAndStorageType(anyString(), anyString(), anyString()))
                .thenReturn(source1);
        assertEquals(sourceRepository.findByNameAndStorageType(source1.getName(), source1.getStorage_types(), source1.getFileType()), source1);
        verify(sourceRepository, times(1)).findByNameAndStorageType(anyString(), anyString(), anyString());
    }

    @Test
    void setStorageType() {
        source1.setStorage_types(testStorage_type);
        assertEquals(source1.getStorage_types(), testStorage_type);
    }

    @Test
    void thatReturnInputStream() throws IOException {
        when(storageSourceService.findSongBySource(any(Source.class))).thenReturn(inputStream);
        storageSourceService.findSongBySource(source1);
        verify(storageSourceService, times(1)).findSongBySource(any(Source.class));
    }

    @Test
    void thatReturnByteArray() throws IOException {
        InputStream targetStream = new ByteArrayInputStream("test".getBytes());
        MockedStatic<IOUtils> utilities = Mockito.mockStatic(IOUtils.class);

        utilities.when(() -> IOUtils.toByteArray(targetStream)).thenReturn(testArray);
        //  IOUtils.toByteArray(targetStream);
        int l = IOUtils.toByteArray(targetStream).length;
        assertEquals(l, "testByteArray".getBytes().length);
//        verify(IOUtils,times(1)).toByteArray(any(Source.class));
    }

    @Test
    void finedSourceByIdInIsExist() {
        when(sourceRepository.findById(anyLong())).thenReturn(source1);
        sourceRepository.findById(source1.getId());
        verify(sourceRepository, times(1)).findById(anyLong());
    }

    @Test
    void isExist() {
        when(storageSourceService.isExist(any(Source.class))).thenReturn(testBoolean = true);
        assertEquals(testBoolean, true);
        storageSourceService.isExist(source1);
        verify(storageSourceService, times(1)).isExist(any(Source.class));
    }


    @Test
    void getListOfSourceInDelete() {
        when(sourceRepository.findAllByName(anyString())).thenReturn(sourceList);
        sourceRepository.findAllByName(source1.getName());
        verify(sourceRepository, times(1)).findAllByName(anyString());
    }

    @Test
    void whenSourceListInDeleteIsEmpty() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            throw new IllegalStateException("source with + name +  do not fined");
        });
        lenient().when(sourceRepository.findAllByName(anyString()))
                .thenReturn(null)
                .thenThrow(exception);
        sourceRepository.findAllByName(source1.getName());
        verify(sourceRepository, times(1)).findAllByName(anyString());

        String expectedMessage = "do not fined";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void whenStorageSourceServiceInDelete() {
        doNothing().when(storageSourceService).delete(any(Source.class));
        storageSourceService.delete(source1);
        verify(storageSourceService, times(1)).delete(any(Source.class));

    }

    @Test
    void whenSourceRepositoryInDelete() {
        doNothing().when(sourceRepository).deleteById(anyLong());
        sourceRepository.deleteById(source1.getId());
        verify(sourceRepository, times(1)).deleteById(anyLong());

    }

    @Test
    void delete() { //TODO



    }
}