package com.ciecwierz.downloader.service;

import com.ciecwierz.downloader.exception.FileInfoFieldNotFoundException;
import com.ciecwierz.downloader.exception.MinioException;
import com.ciecwierz.downloader.model.dto.StoredFileInfoDTO;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageService {

    private final MinioClient minioClient;

    public StoredFileInfoDTO saveFile(byte[] file, String url) {
        LocalDate date = LocalDate.now();
        String bucketName = prepareBucketName(date);
        String objectName = UUID.randomUUID().toString();
        createBucketIfNotExist(bucketName);
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(new ByteArrayInputStream(file), file.length, -1)
                .build();
        try {
            minioClient.putObject(putObjectArgs);
            return new StoredFileInfoDTO(date, objectName, url);
        } catch (Exception e) {
            log.error("Error while saving file to minIO: {}", e.getMessage());
            throw new MinioException(e);
        }
    }

    public byte[] readFile(StoredFileInfoDTO storedFileInfo) {
        validateFieldsInFileInfo(storedFileInfo);

        String bucketName = prepareBucketName(storedFileInfo.date());
        GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                .bucket(bucketName)
                .object(storedFileInfo.filename())
                .build();
        try {
            InputStream inputStream = minioClient.getObject(getObjectArgs);
            return IOUtils.toByteArray(inputStream);
        } catch (Exception e) {
            log.error("Error while reading file from minIO: {}", e.getMessage());
            throw new MinioException(e);
        }
    }

    private void createBucketIfNotExist(String bucketName) {
        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder()
                .bucket(bucketName)
                .build();
        try {
            boolean found = minioClient.bucketExists(bucketExistsArgs);
            if (!found) {
                log.info("Bucket with name {} not found. It will be created now.", bucketName);
                createBucket(bucketName);
            }
        } catch (Exception e) {
            log.error("Error while creating bucket in minIO: {}", e.getMessage());
            throw new RuntimeException("error", e);
        }
    }

    private void createBucket(String bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder()
                .bucket(bucketName)
                .build();
        minioClient.makeBucket(makeBucketArgs);
    }

    private void validateFieldsInFileInfo(StoredFileInfoDTO storedFileInfo) {
        Optional.ofNullable(storedFileInfo.date())
                .orElseThrow(() -> new FileInfoFieldNotFoundException("date"));
        Optional.ofNullable(storedFileInfo.filename())
                .orElseThrow(() -> new FileInfoFieldNotFoundException("filename"));
    }

    private String prepareBucketName(LocalDate date) {
        return date.format(DateTimeFormatter.ISO_DATE);
    }
}
