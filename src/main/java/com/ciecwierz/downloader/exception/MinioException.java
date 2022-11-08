package com.ciecwierz.downloader.exception;

public class MinioException extends RuntimeException {

    private final static String MINIO_EXCEPTION_MESSAGE = "minIO error: %s";

    public MinioException(Throwable t) {
        super(String.format(MINIO_EXCEPTION_MESSAGE, t.getMessage()));
    }
}
