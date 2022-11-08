package com.ciecwierz.downloader.repository;

import com.ciecwierz.downloader.model.DownloadedFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DownloadedFileRepository extends MongoRepository<DownloadedFile, String> {
}
