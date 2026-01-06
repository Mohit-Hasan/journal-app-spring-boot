package com.mohithasan.journalapp.controller;

import com.mohithasan.journalapp.cache.AppCache;
import com.mohithasan.journalapp.entity.User;
import com.mohithasan.journalapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminControllerTests {

    @Mock
    private UserService userService;

    @Mock
    private AppCache appCache;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers_ReturnsUsers() {
        User user1 = new User();
        user1.setEmail("user1@domain.com");
        User user2 = new User();
        user2.setEmail("user2@domain.com");

        List<User> users = Arrays.asList(user1, user2);

        when(userService.getAll()).thenReturn(users);

        ResponseEntity<List<User>> response = adminController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(userService, times(1)).getAll();
    }

    @Test
    void testGetAllUsers_ReturnsNotFound() {
        when(userService.getAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<User>> response = adminController.getAllUsers();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, times(1)).getAll();
    }

    @Test
    void testClearCache() {
        ResponseEntity<Void> response = adminController.clearCache();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(appCache, times(1)).inti();
    }
}
