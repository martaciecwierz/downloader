package com.ciecwierz.downloader.model.dto;

import java.time.LocalDate;

public record StoredFileInfoDTO(LocalDate date, String filename, String url) {
}
