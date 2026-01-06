package com.mohithasan.journalapp.controller;

import com.mohithasan.journalapp.entity.User;
import com.mohithasan.journalapp.repository.UserRepository;
import com.mohithasan.journalapp.service.UserDetailsServiceAuth;
import com.mohithasan.journalapp.utilis.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GoogleAuthControllerTests {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private UserDetailsServiceAuth userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private GoogleAuthController googleAuthController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleGoogleCallback_NewUser() {
        String code = "GFD64565";
        String idToken = "JGJHFJFGH765GGFGHF";
        String email = "test@domain.com";
        String fakeJwt = "HJG6756FGFHYTT54546";

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("id_token", idToken);
        ResponseEntity<Map> tokenResponse = new ResponseEntity<>(tokenMap, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class))).thenReturn(tokenResponse);

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("email", email);
        ResponseEntity<Map> userInfoResponse = new ResponseEntity<>(userInfo, HttpStatus.OK);
        when(restTemplate.getForEntity(contains("id_token=" + idToken), eq(Map.class))).thenReturn(userInfoResponse);

        when(userDetailsService.loadUserByUsername(email)).thenThrow(new RuntimeException());

        when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");
        when(jwtUtil.generateToken(email)).thenReturn(fakeJwt);

        ResponseEntity<?> response = googleAuthController.handleGoogleCallback(code);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, String> body = (Map<String, String>) response.getBody();
        assertEquals(fakeJwt, body.get("token"));

        verify(userRepository).save(any(User.class));
    }

    @Test
    void testHandleGoogleCallback_ExistingUser() {
        String code = "fake-code";
        String idToken = "fake-id-token";
        String email = "test@example.com";
        String fakeJwt = "jwt-token";

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("id_token", idToken);
        ResponseEntity<Map> tokenResponse = new ResponseEntity<>(tokenMap, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class))).thenReturn(tokenResponse);

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("email", email);
        ResponseEntity<Map> userInfoResponse = new ResponseEntity<>(userInfo, HttpStatus.OK);
        when(restTemplate.getForEntity(contains("id_token=" + idToken), eq(Map.class))).thenReturn(userInfoResponse);

        UserDetails mockUser = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(mockUser);
        when(jwtUtil.generateToken(email)).thenReturn(fakeJwt);

        ResponseEntity<?> response = googleAuthController.handleGoogleCallback(code);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, String> body = (Map<String, String>) response.getBody();
        assertEquals(fakeJwt, body.get("token"));
        verify(userRepository, never()).save(any(User.class));
    }
}
