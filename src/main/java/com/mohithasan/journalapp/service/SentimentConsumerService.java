package com.mohithasan.journalapp.service;

import lombok.extern.slf4j.Slf4j;
import com.mohithasan.journalapp.model.SentimentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class SentimentConsumerService {

    private final EmailService emailService;

    @Autowired
    public SentimentConsumerService(EmailService emailService){
        this.emailService = emailService;
    }


    @KafkaListener(topics = "weekly-sentiments", groupId = "weekly-sentiment-group")
    public void consume(SentimentData sentimentData){
        sendEmail(sentimentData);
    }

    private void sendEmail(SentimentData sentimentData) {
        log.error("Email Sent on mail: " + sentimentData.getEmail());
        emailService.sendMail(sentimentData.getEmail(),"Your Sentiment for Last Week.",  sentimentData.getSentiment());
    }
}
