package com.mohithasan.journalapp.scheduler;

import lombok.extern.slf4j.Slf4j;
import com.mohithasan.journalapp.cache.AppCache;
import com.mohithasan.journalapp.entity.JournalEntry;
import com.mohithasan.journalapp.entity.User;
import com.mohithasan.journalapp.enums.Sentiment;
import com.mohithasan.journalapp.model.SentimentData;
import com.mohithasan.journalapp.repository.UserRepositoryImpl;
import com.mohithasan.journalapp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UserScheduler {

    private final EmailService emailService;
    private final UserRepositoryImpl userRepository;
    private final AppCache appCache;
    private final KafkaTemplate<String, SentimentData> kafkaTemplate;

    @Autowired
    public UserScheduler(
            EmailService emailService,
            UserRepositoryImpl userRepository,
            AppCache appCache,
            KafkaTemplate<String, SentimentData> kafkaTemplate) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.appCache = appCache;
        this.kafkaTemplate = kafkaTemplate;
    }



    @Scheduled(cron = "0  0 9 * * SUN")
    public void fetchUsersAndSendMail(){
        List<User> users = userRepository.getUsersForSA();

        for(User user : users){
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> sentiments = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minusDays(70))).map(JournalEntry::getSentiment).collect(Collectors.toList());
            String sentiment = sentiments.toString();

            if(sentiment != null){
                SentimentData sentimentData = SentimentData.builder().email(user.getEmail()).sentiment("Sentiment for last 7 days " + sentiment).build();
                try{
                    kafkaTemplate.send("weekly-sentiments", user.getEmail(), sentimentData);
                }catch (Exception e){
                    emailService.sendMail(user.getEmail(), "Sentiment for last 7 days [F]", sentiment);
                    log.error("Exception while registering on kafka", e);
                }

            }
        }
    }

    @Scheduled(cron = "0 0/10 * ? * *")
    public void clearAppCache(){
        appCache.inti();
    }
}
