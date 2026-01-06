package com.mohithasan.journalapp.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.mohithasan.journalapp.entity.User;
import com.mohithasan.journalapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

class UserDetailsServiceAuthTests{

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceAuth userDetailsService;

    public UserDetailsServiceAuthTests() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_UserExists() {
        User mockUser = new User();
        mockUser.setUserName("mohit");
        mockUser.setPassword("123456");
        mockUser.setRoles(Collections.singletonList("USER"));

        when(userRepository.findByUserName("mohit")).thenReturn(mockUser);

        UserDetails userDetails = userDetailsService.loadUserByUsername("mohit");

        assertEquals("mohit", userDetails.getUsername());
        assertEquals("123456", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByUserName("hasan")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("hasan"));
    }
}
