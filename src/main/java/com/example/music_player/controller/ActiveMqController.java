//package com.example.music_player.controller;
//
//import org.apache.activemq.command.ActiveMQQueue;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.configurationprocessor.json.JSONException;
//import org.springframework.jms.core.JmsTemplate;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.jms.Queue;
//
//@RestController
//@RequestMapping("/rest/publish")
//public class ActiveMqController {
//
//    @Autowired
//    private JmsTemplate jmsTemplate;
//
//    @Value("${activemq.queue.name}")
//    private ActiveMQQueue queue;
//
//
//    @GetMapping(value = "/{message}", produces = "text/html")
//    public String publish(@PathVariable("message") final String message) {
//
////        Click click = new Click();
////        click.setCount(11);
////        click.setName(message);
////        jmsTemplate.convertAndSend(queue, click);
//        return "send is Ok!" + message;
//    }
//}
