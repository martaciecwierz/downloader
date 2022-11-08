package com.ciecwierz.downloader.kafka.producer;

import com.ciecwierz.downloader.kafka.message.DownloadFileKafkaMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DownloadFileProducer {

    @Value("${downloader.kafka.download-file.topic-name}")
    private String topicName;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(DownloadFileKafkaMessage kafkaMessage) {
        kafkaTemplate.send(topicName, kafkaMessage);
    }
}
