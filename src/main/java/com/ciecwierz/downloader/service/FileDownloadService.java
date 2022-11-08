package com.ciecwierz.downloader.service;

import com.ciecwierz.downloader.exception.DownloadException;
import com.ciecwierz.downloader.exception.UrlException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

@Service
@Slf4j
public class FileDownloadService {
    @Value("${downloader.url-connection.connect-timeout}")
    private int connectTimeout;

    @Value("${downloader.url-connection.read-timeout}")
    private int readTimeout;

    public byte[] downloadFile(String urlToDownload) {
        try {
            URL url = getURL(urlToDownload);
            InputStream inputStream = getInputStreamFromConnection(url);
            return copyToByteArray(inputStream);
        } catch (IOException e) {
            log.error("Error while downloading file from url: {}", e.getMessage());
            throw new DownloadException(e);
        }
    }

    private URL getURL(String urlAsString) {
        try {
            return new URL(urlAsString);
        } catch (MalformedURLException e) {
            log.error("Error while preparing url instance: {}", e.getMessage());
            throw new UrlException(e);
        }
    }

    private InputStream getInputStreamFromConnection(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(connectTimeout);
        connection.setReadTimeout(readTimeout);
        connection.connect();
        return connection.getInputStream();
    }

    private byte[] copyToByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, outputStream);
        return outputStream.toByteArray();
    }
}
