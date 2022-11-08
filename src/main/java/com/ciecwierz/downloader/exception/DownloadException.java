package com.ciecwierz.downloader.exception;

public class DownloadException extends RuntimeException {

    private final static String DOWNLOAD_EXCEPTION_MESSAGE = "Download error: %s";

    public DownloadException(Throwable t) {
        super(String.format(DOWNLOAD_EXCEPTION_MESSAGE, t.getMessage()));
    }
}
