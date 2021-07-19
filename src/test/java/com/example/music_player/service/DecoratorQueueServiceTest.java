package com.example.music_player.service;

import com.example.music_player.MusicPlayerApplication;
import com.example.music_player.entity.Source;
import org.apache.catalina.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@ContextConfiguration(classes = MusicPlayerApplication.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Server.class)
class DecoratorQueueServiceTest {

    private Source source;
    private Source source2;
    private byte[] testArray;
    private String testString;
    private Boolean result;

    @Autowired
    private JmsTemplate jmsTemplate;

    @MockBean
    ISourceService sourceService;

//    @Autowired
//    ISourceService sourceService;

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

        testArray = new byte[]{1, 2, 3};
        result = false;
    }

    @Test
    void JMSConvertAndSendMessage() {
        jmsTemplate.convertAndSend("music_player", source);
        jmsTemplate.setReceiveTimeout(10_000);
        assertThat(jmsTemplate.receiveAndConvert("music_player")).isEqualTo(source);
    }

    @Test
    void save() {
        // at work
        Source sourceMock = Mockito.mock(Source.class);
        when(sourceService.save(any(), any(), anyLong())).thenReturn(source2);
        when(sourceMock.getFileType()).thenReturn(source2.getFileType());

        if (!Objects.requireNonNull(sourceMock.getFileType()).equals("audio/mpeg")) {
//            doNothing().when(JMSConvertAndSend(source2));
//        }
//    }
//        private void JMSConvertAndSend(Source source) {
//            jmsTemplate.convertAndSend(queue, source);
//            log.info("file " + source.getName() + "was put in Queue ");
        }




//        assertNotEquals(testSourceService.save(any(), any(), anyLong()), sourceService.save());

//        assertEquals(source2.getName(), "testName2");
    }



    @Test
    void getFileType() {
        String contentType = source.getFileType();
        assertEquals(contentType, "testFileType");
    }

    @Test
    void findByName() throws IOException {
        when(sourceService.findByName(anyString(), anyString(), anyString()))
                .thenReturn(testArray);
        assertEquals(testArray.length, 3);
    }

    @Test
    void isExist() {
        when(sourceService.isExist(anyLong())).thenReturn(result = true);
        assertThat(result).isEqualTo(true);
    }

    @Test
    void delete() {
        when(sourceService.delete(anyString())).thenReturn(testString = "testString");
        assertThat(testString).isEqualTo("testString");
    }
}

