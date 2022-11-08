package com.ciecwierz.downloader.kafka.consumer;

import com.ciecwierz.downloader.kafka.message.DownloadFileKafkaMessage;
import com.ciecwierz.downloader.service.FileManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DownloadFileConsumerService {

    private final FileManagementService fileManagementService;

    @KafkaListener(topics = "${downloader.kafka.download-file.topic-name}", groupId = "${downloader.kafka.download-file.group-id}")
    public void consume(@Payload DownloadFileKafkaMessage kafkaMessage) {
        log.info("Consume message with url: {}", kafkaMessage.url());

        fileManagementService.store(kafkaMessage.url());
    }
}
