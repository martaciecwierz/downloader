package com.ciecwierz.downloader.model.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record FileToDownloadDTO(@NotNull @NotEmpty String url) {
}
