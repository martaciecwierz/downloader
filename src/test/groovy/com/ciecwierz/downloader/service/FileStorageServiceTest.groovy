package com.ciecwierz.downloader.service

import com.ciecwierz.downloader.exception.FileInfoFieldNotFoundException
import com.ciecwierz.downloader.exception.MinioException
import com.ciecwierz.downloader.model.dto.StoredFileInfoDTO
import io.minio.GetObjectArgs
import io.minio.GetObjectResponse
import io.minio.MinioClient
import io.minio.PutObjectArgs
import spock.lang.Specification

import java.time.LocalDate

class FileStorageServiceTest extends Specification {

    static final def TEST_BYTES = "test file".getBytes()
    static final def TEST_URL = "https://test.url"
    static final def TEST_FILENAME = "test_filename"
    static final def TEST_BUCKET = "07-11-2022"

    def minioClient = Stub(MinioClient.class)
    def fileStorageService = new FileStorageService(minioClient)

    def "should read file from minio"() {
        given:
            def storedFileInfo = new StoredFileInfoDTO(LocalDate.now(), TEST_FILENAME, TEST_URL)
            minioClient.getObject(_ as GetObjectArgs) >>
                    new GetObjectResponse(null, TEST_BUCKET, "pl", TEST_FILENAME, new ByteArrayInputStream(TEST_BYTES))
        when:
            fileStorageService.readFile(storedFileInfo)
        then:
            noExceptionThrown()
    }

    def "should save file to minio"() {
        when:
            def fileInfo = fileStorageService.saveFile(TEST_BYTES, TEST_URL)
        then:
            fileInfo.filename() != null
            fileInfo.date() != null
            fileInfo.url() == TEST_URL
    }

    def "should throw exception when minio client gives a error while put object"() {
        given:
            minioClient.putObject(_ as PutObjectArgs) >> { throw new IOException("exception") }
        when:
            fileStorageService.saveFile(TEST_BYTES, TEST_URL)
        then:
            def ex = thrown(MinioException.class)
            ex.message == "minIO error: exception"
    }

    def "should throw exception when field of fileInfo is null"() {
        given:
            def storedFileInfo = new StoredFileInfoDTO(date, filename, TEST_URL)
        when:
            fileStorageService.readFile(storedFileInfo)
        then:
            def ex = thrown(FileInfoFieldNotFoundException.class)
            ex.getMessage() == String.format("Field %s not found", field)
        where:
            date            | filename      | field
            null            | TEST_FILENAME | "date"
            LocalDate.now() | null          | "filename"
            null            | null          | "date"
    }

}
