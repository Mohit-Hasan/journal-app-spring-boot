package com.mohithasan.journalapp.controller;

import com.mohithasan.journalapp.dto.UserDTO;
import com.mohithasan.journalapp.entity.User;
import com.mohithasan.journalapp.service.UserDetailsServiceAuth;
import com.mohithasan.journalapp.service.UserService;
import com.mohithasan.journalapp.utilis.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PublicControllerTests {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsServiceAuth userDetailsService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserService userService;

    @InjectMocks
    private PublicController publicController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHealthCheck_ReturnsOk() {
        String response = publicController.healthCheck();
        assertEquals("ok", response);
    }

    @Test
    void testSignup_Success() {
        UserDTO dto = new UserDTO();
        dto.setUserName("testuser");
        dto.setPassword("password");

        User mockUser = new User();
        mockUser.setUserName("testuser");

        when(userService.saveNewEntry(dto)).thenReturn(mockUser);

        ResponseEntity<Object> response = publicController.signup(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
    }

    @Test
    void testSignup_Failure() {
        UserDTO dto = new UserDTO();
        dto.setUserName("testuser");
        dto.setPassword("password");

        when(userService.saveNewEntry(dto)).thenThrow(new IllegalArgumentException("User already exists"));

        ResponseEntity<Object> response = publicController.signup(dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Map.of("error", "User already exists"), response.getBody());
    }

    @Test
    void testLogin_Success() {
        UserDTO dto = new UserDTO();
        dto.setUserName("testuser");
        dto.setPassword("password");

        UserDetails mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn("testuser");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);

        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(mockUserDetails);
        when(jwtUtil.generateToken("testuser")).thenReturn("mock-jwt-token");

        ResponseEntity<Object> response = publicController.login(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("mock-jwt-token", response.getBody());
    }

    @Test
    void testLogin_Failure() {
        UserDTO dto = new UserDTO();
        dto.setUserName("wronguser");
        dto.setPassword("wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Bad credentials"));

        ResponseEntity<Object> response = publicController.login(dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Map.of("error", "Incorrect username or password"), response.getBody());
    }

}
