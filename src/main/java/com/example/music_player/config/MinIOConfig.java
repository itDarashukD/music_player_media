package com.example.music_player.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinIOConfig {

    @Value("${cloud.MiniO.credentials.endpoint}")
    private String endpoint;
    @Value("${cloud.MiniO.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.MiniO.credentials.secret-key}")
    private String accessSecret;

    @Bean
    public MinioClient minioClient() {
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint(endpoint)
                        .credentials(accessKey, accessSecret)
                        .build();
        return minioClient;
    }
}
