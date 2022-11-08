package com.ciecwierz.downloader.service

import com.ciecwierz.downloader.exception.FileNotFoundException
import com.ciecwierz.downloader.repository.DownloadedFileRepository
import spock.lang.Specification

class FileManagementServiceTest extends Specification {

    def downloadedFileRepository = Stub(DownloadedFileRepository.class)
    def fileStorageService = Stub(FileStorageService.class)
    def fileDownloadService = Stub(FileDownloadService.class)

    def fileManagementService = new FileManagementService(downloadedFileRepository, fileStorageService, fileDownloadService)

    def "should throw exception when getting non existing file"() {
        given:
            downloadedFileRepository.findById(_ as String) >> Optional.empty()
        when:
            fileManagementService.getFile("fileId")
        then:
            def ex = thrown(FileNotFoundException.class)
            ex.getMessage() == "File is not found"
    }
}
