package com.mohithasan.journalapp.scheduler;

import com.mohithasan.journalapp.cache.AppCache;
import com.mohithasan.journalapp.entity.JournalEntry;
import com.mohithasan.journalapp.entity.User;
import com.mohithasan.journalapp.enums.Sentiment;
import com.mohithasan.journalapp.model.SentimentData;
import com.mohithasan.journalapp.repository.UserRepositoryImpl;
import com.mohithasan.journalapp.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.kafka.core.KafkaTemplate;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;

class UserSchedulerTests {

    @Mock
    private EmailService emailService;

    @Mock
    private UserRepositoryImpl userRepository;

    @Mock
    private AppCache appCache;

    @Mock
    private KafkaTemplate<String, SentimentData> kafkaTemplate;

    @InjectMocks
    private UserScheduler userScheduler;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFetchUsersAndSendMail_SendsToKafka() {
        JournalEntry entry = new JournalEntry();
        entry.setDate(LocalDateTime.now().minusDays(1));
        entry.setSentiment(Sentiment.HAPPY);

        User user = new User();
        user.setEmail("test@example.com");
        user.setJournalEntries(Collections.singletonList(entry));

        when(userRepository.getUsersForSA()).thenReturn(Collections.singletonList(user));
        userScheduler.fetchUsersAndSendMail();

        ArgumentCaptor<SentimentData> captor = ArgumentCaptor.forClass(SentimentData.class);
        verify(kafkaTemplate).send(eq("weekly-sentiments"), eq("test@example.com"), captor.capture());
        SentimentData data = captor.getValue();
        assert data.getEmail().equals("test@example.com");
        assert data.getSentiment().contains("HAPPY");
        verify(emailService, never()).sendMail(anyString(), anyString(), anyString());
    }

    @Test
    void testClearAppCache_CallsInit() {
        userScheduler.clearAppCache();
        verify(appCache).inti();
    }
}
