package com.ciecwierz.downloader.exception;

public class FileInfoFieldNotFoundException extends RuntimeException {

    private static final String FILE_INFO_FIELD_NOT_FOUND_MESSAGE = "Field %s not found";

    public FileInfoFieldNotFoundException(String field) {
        super(String.format(FILE_INFO_FIELD_NOT_FOUND_MESSAGE, field));
    }
}
