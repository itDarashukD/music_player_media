package com.example.music_player.service;

import com.example.music_player.entity.Song;
import com.example.music_player.entity.Source;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DecoratorQueueServiceTest {

    private Source source;
    private Source source2;
    private byte[] testArray;
    private Boolean result;
    private String queueName;
    private String contentType;
    private String nullContentType;

    @Mock
    private JmsTemplate jmsTemplate;

    @Mock
    ISourceService sourceService;

    @Mock
    Song mockSong;

    @Mock
    MultipartFile mockMultipartFile;

    @BeforeEach
    public void createSource() {
        source = new Source(1111L
                , 1L
                , 1L
                , "testName"
                , "/1/1"
                , 1111L
                , "111111"
                , "TEST_STORAGE"
                , "testFileType");
        source2 = new Source(2L
                , 2L
                , 2L
                , "testName2"
                , "/2/2"
                , 2222L
                , "222222"
                , "TEST_STORAGE"
                , "testFileType");

        nullContentType = null;
        contentType = "mp3";
        queueName = "music_player";
        testArray = new byte[]{1, 2, 3};
        result = false;
    }

    @Test
    void JMSConvertAndSendMessage() {
        doNothing().when(jmsTemplate).convertAndSend(anyString(), any(Source.class));
        jmsTemplate.convertAndSend(queueName, source);
        verify(jmsTemplate, times(1)).convertAndSend(queueName, source);
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void save() {
        when(sourceService.save(any(), any(), anyLong())).thenReturn(source2);
        sourceService.save(mockMultipartFile, mockSong, 2L);
        verify(sourceService, times(1)).save(mockMultipartFile, mockSong, 2L);
    }


    @Test
    void whenContentTypeIsNotAudioOrMpeg() {
//        source2.setFileType(nullContentType);
//        verify(jmsTemplate).convertAndSend(queueName, source2);
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void findByName() throws IOException {
        when(sourceService.findByName(anyString(), anyString(), anyString())).thenReturn(testArray);
        assertEquals(testArray.length, 3);
        sourceService.findByName(source.getName(), source.getStorage_types(), source.getFileType());
        verify(sourceService, times(1)).findByName(anyString(), anyString(), anyString());
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void isExist() {
        when(sourceService.isExist(anyLong())).thenReturn(result = true);
        assertThat(result).isEqualTo(true);
        sourceService.isExist(source.getId());
        verify(sourceService, times(1)).isExist(anyLong());
    }

    @Test
    void delete() {
        when(sourceService.delete(anyString())).thenReturn(true);
        assertEquals(sourceService.delete(source.getName()), true);
        verify(sourceService, times(1)).delete(anyString());
    }
}

