package com.ase.exagrad.studentservice.services;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;
import com.ase.exagrad.studentservice.exceptions.StorageException;
import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MinioService {

  // Configuration constants
  private static final int DEFAULT_URL_EXPIRY_HOURS = 1;
  private static final long PART_SIZE = -1; // Use default part size
  private final MinioClient minioClient;

  /**
   * Upload file to MinIO bucket
   *
   * @param bucketName  the bucket name
   * @param objectKey   the object key/path
   * @param inputStream the file input stream
   * @param size        the file size
   * @param contentType the content type
   * @throws StorageException if upload fails
   */
  public void uploadFile(
      String bucketName,
      String objectKey,
      InputStream inputStream,
      long size,
      String contentType) {
    try {
      ensureBucketExists(bucketName);

      minioClient.putObject(
          PutObjectArgs.builder().bucket(bucketName).object(objectKey).stream(
                  inputStream, size, PART_SIZE)
              .contentType(contentType)
              .build());

      log.info(
          "Successfully uploaded file to MinIO: bucket={}, key={}, size={}",
          bucketName,
          objectKey,
          size);
    } catch (Exception e) {
      log.error(
          "Failed to upload file to MinIO: bucket={}, key={}", bucketName, objectKey, e);
      throw new StorageException("Failed to upload file: " + objectKey, e);
    }
  }

  /**
   * Generate presigned URL for file download
   *
   * @param bucketName the bucket name
   * @param objectKey  the object key/path
   * @return presigned URL valid for 1 hour
   * @throws StorageException if URL generation fails
   */
  public String getFileUrl(String bucketName, String objectKey) {
    return getFileUrl(bucketName, objectKey, DEFAULT_URL_EXPIRY_HOURS, TimeUnit.HOURS);
  }

  /**
   * Generate presigned URL for file download with custom expiry
   *
   * @param bucketName the bucket name
   * @param objectKey  the object key/path
   * @param expiry     the expiry time
   * @param timeUnit   the time unit for expiry
   * @return presigned URL
   * @throws StorageException if URL generation fails
   */
  public String getFileUrl(String bucketName, String objectKey, int expiry, TimeUnit timeUnit) {
    try {
      String url =
          minioClient.getPresignedObjectUrl(
              GetPresignedObjectUrlArgs.builder()
                  .method(Method.GET)
                  .bucket(bucketName)
                  .object(objectKey)
                  .expiry(expiry, timeUnit)
                  .build());

      log.debug(
          "Generated presigned URL for bucket={}, key={}, expires in {} {}",
          bucketName,
          objectKey,
          expiry,
          timeUnit.name().toLowerCase());
      return url;
    } catch (Exception e) {
      log.error(
          "Failed to generate presigned URL: bucket={}, key={}",
          bucketName,
          objectKey,
          e);
      throw new StorageException(
          "Failed to generate presigned URL for: " + objectKey, e);
    }
  }

  /**
   * Delete file from MinIO bucket
   *
   * @param bucketName the bucket name
   * @param objectKey  the object key/path
   * @throws StorageException if deletion fails
   */
  public void deleteFile(String bucketName, String objectKey) {
    try {
      minioClient.removeObject(
          RemoveObjectArgs.builder().bucket(bucketName).object(objectKey).build());

      log.info(
          "Successfully deleted file from MinIO: bucket={}, key={}",
          bucketName,
          objectKey);
    } catch (Exception e) {
      log.error(
          "Failed to delete file from MinIO: bucket={}, key={}",
          bucketName,
          objectKey,
          e);
      throw new StorageException("Failed to delete file: " + objectKey, e);
    }
  }

  /**
   * Check if file exists in MinIO bucket
   *
   * @param bucketName the bucket name
   * @param objectKey  the object key/path
   * @return true if file exists, false otherwise
   */
  public boolean fileExists(String bucketName, String objectKey) {
    try {
      minioClient.statObject(
          StatObjectArgs.builder().bucket(bucketName).object(objectKey).build());
      return true;
    } catch (Exception e) {
      log.debug(
          "File does not exist or is inaccessible: bucket={}, key={}",
          bucketName,
          objectKey);
      return false;
    }
  }

  /**
   * Ensure bucket exists, create if it doesn't
   *
   * @param bucketName the bucket name
   * @throws Exception if bucket operations fail
   */
  private void ensureBucketExists(String bucketName) throws Exception {
    boolean bucketExists =
        minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

    if (!bucketExists) {
      minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
      log.info("Created new MinIO bucket: {}", bucketName);
    }
  }

}
