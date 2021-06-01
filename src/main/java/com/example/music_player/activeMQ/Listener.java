package com.example.music_player.activeMQ;

import com.example.music_player.entity.Source;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import javax.jms.JMSException;


@Component
@Slf4j
public class Listener {

    @Autowired
    private IListenerService listenerService;

     @JmsListener(destination = "music_player_queue", containerFactory = "jsaFactory")
    public void receive(Source source) {
        log.info("IN class Listener receive() : object received : {}", source.getName() + " " + source.getStorage_types());

        listenerService.getFileBySourceFromListener(source);
    }

// Two listeners here
//    @JmsListener(destination = "music_player_queue", containerFactory="foo1")
//    public void foo1(Source source) throws JMSException {
//        log.info("IN class Listener receive() : object received : {}", source.getName() + " " + source.getStorage_types());
//        listenerService.getFileBySourceFromListener(source);
//    }
//
//    @JmsListener(destination = "music_player_queue", containerFactory="foo2")
//    public void foo12(Source source) throws JMSException {
//        log.info("IN class Listener receive() : object received : {}", source.getName() + " " + source.getStorage_types());
//        listenerService.getFileBySourceFromListener(source);
//    }
}

