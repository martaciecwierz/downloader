package com.ciecwierz.downloader.controller;

import com.ciecwierz.downloader.kafka.message.DownloadFileKafkaMessage;
import com.ciecwierz.downloader.kafka.producer.DownloadFileProducer;
import com.ciecwierz.downloader.model.dto.FileCommonDataDTO;
import com.ciecwierz.downloader.model.dto.FileToDownloadDTO;
import com.ciecwierz.downloader.service.FileManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/file")
@RequiredArgsConstructor
public class FileController {

    private final FileManagementService fileManagementService;
    private final DownloadFileProducer downloadFileProducer;

    @PostMapping
    public void storeFile(@RequestBody @Valid FileToDownloadDTO dto) {
        downloadFileProducer.send(new DownloadFileKafkaMessage(dto.url()));
    }

    @GetMapping
    public List<FileCommonDataDTO> getAllFilesCommonData() {
        return fileManagementService.getAllFiles();
    }

    @GetMapping(
            value = "{fileId}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<byte[]> getFileById(@PathVariable String fileId) {
        return ResponseEntity.ok()
                .headers(getHeaders())
                .body(fileManagementService.getFile(fileId));
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-disposition", "attachment;filename=response");
        return headers;
    }
}
