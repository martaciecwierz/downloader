package com.ciecwierz.downloader.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Getter
@Builder
public class DownloadedFile {

    @Id
    private String id;
    private LocalDate date;
    private String filename;
    private String url;
}
