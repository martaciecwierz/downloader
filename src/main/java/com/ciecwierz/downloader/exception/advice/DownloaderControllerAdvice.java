package com.ciecwierz.downloader.exception.advice;

import com.ciecwierz.downloader.exception.FileInfoFieldNotFoundException;
import com.ciecwierz.downloader.exception.FileNotFoundException;
import com.ciecwierz.downloader.exception.MinioException;
import com.ciecwierz.downloader.exception.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@ControllerAdvice
public class DownloaderControllerAdvice {

    private final static String VALIDATION_MESSAGE = "Validation error for fields: %s";

    @ExceptionHandler({FileNotFoundException.class, FileInfoFieldNotFoundException.class})
    public ResponseEntity<ErrorResponse> onNotFound(RuntimeException exception, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> onMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        String fields = exception.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getField)
                .collect(Collectors.joining(", "));
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST, VALIDATION_MESSAGE, request.getRequestURI(), fields);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MinioException.class)
    public ResponseEntity<ErrorResponse> onMinioException(MinioException exception, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
