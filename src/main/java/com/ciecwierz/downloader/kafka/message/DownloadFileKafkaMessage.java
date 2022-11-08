package com.ciecwierz.downloader.kafka.message;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DownloadFileKafkaMessage(@JsonProperty("url") String url) {
}
