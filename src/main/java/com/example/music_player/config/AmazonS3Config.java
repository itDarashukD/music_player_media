package com.example.music_player.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3Config {

    @Value("${cloud.AmazonS3.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.AmazonS3.credentials.secret-key}")
    private String accessSecret;
    @Value("${cloud.AmazonS3.credentials.region}")
    private String region;
    @Value("${cloud.AmazonS3.credentials.service.endpoint}")
    private String endpoint;

    AWSCredentials credentials = new BasicAWSCredentials(accessKey, accessSecret);

//    ClientConfiguration clientConfiguration = new ClientConfiguration();
//    clientConfiguration.setSignerOverride("AWSS3V4SignerType");

    @Bean
    public AmazonS3 s3Client() {
        AmazonS3 s3Client = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .withPathStyleAccessEnabled(true)
//                .withClientConfiguration(clientConfiguration)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
        return s3Client;
    }
}
