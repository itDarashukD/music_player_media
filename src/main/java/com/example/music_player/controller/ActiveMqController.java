package com.example.music_player.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Queue;

@RestController
@RequestMapping("/rest/publish")
public class ActiveMqController {

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Queue queue;

    @GetMapping(value = "/{message}",produces = "text/html")
    public String publishMessage(@PathVariable("message") String message) {
        jmsTemplate.convertAndSend(queue, message);
        return message + " pulished!";
    }
}
