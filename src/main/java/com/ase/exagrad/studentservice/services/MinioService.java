package com.ase.exagrad.studentservice.services;

import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;

    public void uploadFile(
            String bucketName,
            String objectKey,
            InputStream inputStream,
            long size,
            String contentType) {
        try {
            // Ensure bucket exists
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("Created bucket '{}'", bucketName);
            }

            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName).object(objectKey).stream(
                                    inputStream, size, -1)
                            .contentType(contentType)
                            .build());
            log.info("Uploaded file to MinIO: bucket={}, key={}", bucketName, objectKey);
        } catch (Exception e) {
            log.error("Error uploading file to MinIO", e);
            throw new RuntimeException(e);
        }
    }

    public String getFileUrl(String bucketName, String objectKey) {
        try {
            String url =
                    minioClient.getPresignedObjectUrl(
                            GetPresignedObjectUrlArgs.builder()
                                    .method(Method.GET)
                                    .bucket(bucketName)
                                    .object(objectKey)
                                    .expiry(1, TimeUnit.HOURS)
                                    .build());
            log.info("Generated MinIO presigned URL: {}", url);
            return url;
        } catch (Exception e) {
            log.error("Error generating presigned URL", e);
            throw new RuntimeException(e);
        }
    }
}
