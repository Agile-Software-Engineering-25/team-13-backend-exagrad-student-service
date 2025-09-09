package com.ase.exagrad.studentservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "minio-connection")
public class MinioConnectionProperties {
    private String endpoint;
    private String accessKey;
    private String secretKey;
}
