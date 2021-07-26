package com.example.music_player.service;

import com.example.music_player.entity.Song;
import com.example.music_player.entity.Source;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.jms.Destination;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class DecoratorQueueServiceTest {

    private Source source1;
    private Source source2;
    private byte[] testArray;
    private Boolean result;

    @Mock
    JmsTemplate jmsTemplate;

    @Mock
    ISourceService sourceService;

    @Mock
    Song mockSong;

    @Mock
    MultipartFile mockMultipartFile;

    @Mock
    ActiveMQQueue queue;

    @Mock
    Source mockSource;

    @Mock
    Destination destination;

    @InjectMocks
    DecoratorQueueService decoratorQueueService;

    @BeforeEach
    public void createSource() {

        source1 = new Source(1111L
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

        testArray = new byte[]{1, 2, 3};
        result = false;
    }

    @Test
    void saveWhenContentTypeEqualsAudioOrMpeg() {
        when(sourceService.save(mockMultipartFile, mockSong, mockSong.getId())).thenReturn(source2);
        source2.setFileType("audio/mpeg");
        decoratorQueueService.save(mockMultipartFile, mockSong, mockSong.getId());
        verify(sourceService, times(1)).save(mockMultipartFile, mockSong, mockSong.getId());
        verify(jmsTemplate, never()).convertAndSend(destination, source1);
    }

    @Test
    void saveWhenContentTypeNotAudioOrMpeg() {
        when(sourceService.save(mockMultipartFile, mockSong, mockSong.getId())).thenReturn(source1);
        source1.setFileType("notAudioOrMpeg");
        ReflectionTestUtils.setField(decoratorQueueService, "queue", queue);
        ReflectionTestUtils.setField(decoratorQueueService, "jmsTemplate", jmsTemplate);
        doNothing().when(jmsTemplate).convertAndSend(queue, source1);

        decoratorQueueService.save(mockMultipartFile, mockSong, mockSource.getId());
        verify(jmsTemplate, times(1)).convertAndSend(queue, source1);
    }

    @Test
    void findByName() throws IOException {
        when(sourceService.findByName(anyString(), anyString(), anyString())).thenReturn(testArray);
        assertEquals(testArray.length, 3);
        decoratorQueueService.findByName(source1.getName(), source1.getStorage_types(), source1.getStorage_types());
        verify(sourceService, times(1))
                .findByName(source1.getName(), source1.getStorage_types(), source1.getStorage_types());
    }

    @Test
    void isExist() {
        when(sourceService.isExist(anyLong())).thenReturn(result = true);
        assertThat(result).isEqualTo(true);
        decoratorQueueService.isExist(source1.getId());
        verify(sourceService, times(1)).isExist(source1.getId());
    }

    @Test
    void delete() {
        when(sourceService.delete(anyString())).thenReturn(true);
        assertEquals(decoratorQueueService.delete(source1.getName()), true);
        verify(sourceService, times(1)).delete(source1.getName());
    }
}

