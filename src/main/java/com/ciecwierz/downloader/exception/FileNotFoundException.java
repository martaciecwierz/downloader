package com.ciecwierz.downloader.exception;

public class FileNotFoundException extends RuntimeException {

    private final static String FILE_NOT_FOUND = "File is not found";

    public FileNotFoundException() {
        super(FILE_NOT_FOUND);
    }
}
