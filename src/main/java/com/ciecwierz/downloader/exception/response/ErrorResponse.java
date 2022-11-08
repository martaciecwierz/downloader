package com.ciecwierz.downloader.exception.response;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime timestamp, int status, String error, String path) {

    public ErrorResponse(HttpStatus status, String error, String path, Object... args) {
        this(LocalDateTime.now(), status.value(), String.format(error, args), path);
    }
}
