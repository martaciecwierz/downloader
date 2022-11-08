package com.ciecwierz.downloader.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
    @Value("${downloader.minio.url}")
    private String url;

    @Value("${downloader.minio.access-key}")
    private String accessKey;

    @Value("${downloader.minio.secret-key}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }
}
