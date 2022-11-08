package com.ciecwierz.downloader.exception;

public class UrlException extends RuntimeException {

    private final static String URL_EXCEPTION_MESSAGE = "URL exception: %s";

    public UrlException(Throwable t) {
        super(String.format(URL_EXCEPTION_MESSAGE, t.getMessage()));
    }
}
