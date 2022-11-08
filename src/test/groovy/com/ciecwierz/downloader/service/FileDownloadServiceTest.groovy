package com.ciecwierz.downloader.service

import com.ciecwierz.downloader.exception.UrlException
import spock.lang.Specification

class FileDownloadServiceTest extends Specification {

    def fileDownloadService = new FileDownloadService()

    def "should throw exception when url hasn't got protocol"() {
        when:
            fileDownloadService.downloadFile("url.org")
        then:
            def ex = thrown(UrlException.class)
            ex.getMessage().contains("no protocol")
    }
}
