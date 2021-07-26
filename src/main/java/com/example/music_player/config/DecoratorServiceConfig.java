package com.example.music_player.config;

import com.example.music_player.service.DecoratorQueueService;

import com.example.music_player.service.ISourceService;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class DecoratorServiceConfig {

    @Bean
    @Primary
    public ISourceService decorator(ISourceService sourceService){
        return new DecoratorQueueService(sourceService);
    }

}
