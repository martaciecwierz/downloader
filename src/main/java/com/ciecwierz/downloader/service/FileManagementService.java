package com.ciecwierz.downloader.service;

import com.ciecwierz.downloader.exception.FileNotFoundException;
import com.ciecwierz.downloader.model.DownloadedFile;
import com.ciecwierz.downloader.model.dto.FileCommonDataDTO;
import com.ciecwierz.downloader.model.dto.StoredFileInfoDTO;
import com.ciecwierz.downloader.repository.DownloadedFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FileManagementService {

    private final DownloadedFileRepository downloadedFileRepository;
    private final FileStorageService fileStorageService;
    private final FileDownloadService fileDownloadService;

    public void store(String url) {
        url = addProtocolIfNotExist(url);
        byte[] bytes = fileDownloadService.downloadFile(url);
        StoredFileInfoDTO storedFileInfo = fileStorageService.saveFile(bytes, url);
        saveFileInDb(storedFileInfo);
    }

    public List<FileCommonDataDTO> getAllFiles() {
        List<DownloadedFile> downloadedFiles = downloadedFileRepository.findAll();
        return downloadedFiles.stream()
                .map(file -> new FileCommonDataDTO(file.getId(), getFormattedDate(file.getDate()), file.getUrl()))
                .collect(Collectors.toList());
    }

    public byte[] getFile(String id) {
        DownloadedFile downloadedFile = downloadedFileRepository.findById(id)
                .orElseThrow(FileNotFoundException::new);
        StoredFileInfoDTO storedFileInfo = new StoredFileInfoDTO(
                downloadedFile.getDate(),
                downloadedFile.getFilename(),
                downloadedFile.getUrl()
        );
        return fileStorageService.readFile(storedFileInfo);
    }

    private void saveFileInDb(StoredFileInfoDTO fileInfo) {
        DownloadedFile downloadedFile = DownloadedFile.builder()
                .date(fileInfo.date())
                .filename(fileInfo.filename())
                .url(fileInfo.url())
                .build();
        downloadedFileRepository.save(downloadedFile);
    }

    private String getFormattedDate(LocalDate date) {
        return date.format(DateTimeFormatter.ISO_DATE);
    }

    private String addProtocolIfNotExist(String url) {
        return validateUrlProtocol(url) ? url : addProtocol(url);
    }

    private boolean validateUrlProtocol(String url) {
        String protocolRegex = "^(http|https)://";
        Pattern pattern = Pattern.compile(protocolRegex);
        Matcher matcher = pattern.matcher(url);
        return matcher.find();
    }

    private String addProtocol(String url) {
        return "https://" + url;
    }
}
