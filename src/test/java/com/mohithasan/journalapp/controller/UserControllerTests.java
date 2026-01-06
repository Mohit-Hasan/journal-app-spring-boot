package com.mohithasan.journalapp.controller;

import com.mohithasan.journalapp.dto.UserDTO;
import com.mohithasan.journalapp.entity.User;
import com.mohithasan.journalapp.repository.UserRepository;
import com.mohithasan.journalapp.service.UserService;
import com.mohithasan.journalapp.service.WeatherService;
import com.mohithasan.journalapp.api.response.WeatherResponse;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTests {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WeatherService weatherService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void testUpdateUser_Success() {
        String username = "testuser";
        when(authentication.getName()).thenReturn(username);

        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("newUser");
        userDTO.setEmail("new@test.com");
        userDTO.setPassword("newpass");

        User existingUser = User.builder()
                .id(new ObjectId())
                .userName(username)
                .email("old@test.com")
                .password("oldpass")
                .build();

        when(userService.findByUserName(username)).thenReturn(existingUser);
        doNothing().when(userService).saveEntry(existingUser);

        ResponseEntity<User> response = userController.updateUser(userDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("newUser", response.getBody().getUserName());
        assertEquals("new@test.com", response.getBody().getEmail());
        assertEquals("newpass", response.getBody().getPassword());
        verify(userService, times(1)).saveEntry(existingUser);
    }

    @Test
    void testUpdateUser_UserNotFound() {
        when(authentication.getName()).thenReturn("nonexistent");
        when(userService.findByUserName("nonexistent")).thenReturn(null);

        UserDTO userDTO = new UserDTO();
        ResponseEntity<User> response = userController.updateUser(userDTO);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testDeleteUserById_Success() {
        String username = "testuser";
        String password = "123456";
        when(authentication.getName()).thenReturn(username);

        User user = User.builder()
                .id(new ObjectId())
                .userName(username)
                .password(password)
                .build();

        when(userRepository.findByUserName(username)).thenReturn(user);
        doNothing().when(userService).deleteByID(user.getId());

        ResponseEntity<Void> response = userController.deleteUserById();

        assertEquals(204, response.getStatusCodeValue());
        verify(userService, times(1)).deleteByID(user.getId());
    }

    @Test
    void testGreeting_WithWeather() {
        String username = "testuser";
        when(authentication.getName()).thenReturn(username);

        WeatherResponse.Current current = new WeatherResponse.Current();
        current.setFeelsLike(25);
        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setCurrent(current);

        when(weatherService.getWeather("Khulna")).thenReturn(weatherResponse);

        ResponseEntity<Object> response = userController.greeting();

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Hi testuser"));
        assertTrue(response.getBody().toString().contains("25"));
    }

    @Test
    void testGreeting_WithoutWeather() {
        String username = "testuser";
        when(authentication.getName()).thenReturn(username);

        when(weatherService.getWeather("Khulna")).thenReturn(null);

        ResponseEntity<Object> response = userController.greeting();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Hi testuser", response.getBody());
    }
}
