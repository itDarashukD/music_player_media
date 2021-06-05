package com.example.music_player.config;

import com.example.music_player.repository.ISourceRepository;
import com.example.music_player.service.DecoratorQueueService;

import com.example.music_player.service.ISourceService;
import com.example.music_player.storage.IStorageSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DecoratorServiceConfig {

    @Bean
  //  @ConditionalOnExpression("${queue.enabled} = true")
    @Primary
    public ISourceService decorator(ISourceService sourceService){
        return new DecoratorQueueService(sourceService);
    }

}
