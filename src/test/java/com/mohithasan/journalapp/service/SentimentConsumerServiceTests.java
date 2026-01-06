package com.mohithasan.journalapp.service;

import com.mohithasan.journalapp.model.SentimentData;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;

class SentimentConsumerServiceTests {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private SentimentConsumerService consumerService;

    @Test
    void testConsume_callsSendEmail() {
        MockitoAnnotations.openMocks(this);

        SentimentData data = new SentimentData();
        data.setEmail("test@domain.com");
        data.setSentiment("Happy");
        consumerService.consume(data);

        verify(emailService, times(1))
                .sendMail("test@domain.com", "Your Sentiment for Last Week.", "Happy");
    }
}
