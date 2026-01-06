package com.mohithasan.journalapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.RedisTemplate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class RedisServiceTests {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOps;

    @InjectMocks
    private RedisService redisService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
    }

    @Test
    void testSetAndGet() {
        String key = "user";
        String json = "{\"name\":\"Mohit\"}";

        doNothing().when(valueOps).set(eq(key), anyString(), anyLong(), any());

        redisService.set(key, new User("Mohit"), 60L);
        verify(valueOps, times(1)).set(eq(key), anyString(), eq(60L), any());

        when(valueOps.get(key)).thenReturn(json);
        User user = redisService.get(key, User.class);
        assertNotNull(user);
        assertEquals("Mohit", user.getName());
    }

    static class User {
        private String name;
        public User() {}
        public User(String name){ this.name = name; }
        public String getName() { return name; }
        public void setName(String name){ this.name = name; }
    }
}
