package com.mohithasan.journalapp.repository;

import com.mohithasan.journalapp.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRepositoryImplTests {

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private UserRepositoryImpl userRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUsersForSA_ReturnsExpectedUsers() {
        User user1 = new User();
        user1.setEmail("test1@domain.com");
        user1.setSentimentAnalysis(true);

        User user2 = new User();
        user2.setEmail("test2@domain.com");
        user2.setSentimentAnalysis(true);

        List<User> mockUsers = Arrays.asList(user1, user2);
        when(mongoTemplate.find(any(Query.class), eq(User.class))).thenReturn(mockUsers);
        List<User> result = userRepository.getUsersForSA();

        assertEquals(2, result.size());
        verify(mongoTemplate).find(any(Query.class), eq(User.class));
    }
}
