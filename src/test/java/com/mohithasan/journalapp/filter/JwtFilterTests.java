package com.mohithasan.journalapp.filter;

import com.mohithasan.journalapp.utilis.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.FilterChain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtFilterTests {

    private JwtFilter jwtFilter;
    private JwtUtil jwtUtil;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        jwtUtil = mock(JwtUtil.class);
        userDetailsService = mock(UserDetailsService.class);
        jwtFilter = new JwtFilter(userDetailsService, jwtUtil);

        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldSetAuthenticationWhenTokenIsValid() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer valid-token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        when(jwtUtil.extractUsername("valid-token")).thenReturn("mohith");
        when(jwtUtil.validateToken("valid-token")).thenReturn(true);
        when(userDetailsService.loadUserByUsername("mohith"))
                .thenReturn(new User("mohith", "pass", new java.util.ArrayList<>()));

        jwtFilter.doFilter(request, response, chain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("mohith",
                SecurityContextHolder.getContext().getAuthentication().getName());

        verify(chain).doFilter(request, response);
    }

    @Test
    void shouldNotSetAuthenticationWhenNoHeader() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        jwtFilter.doFilter(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(request, response);
    }
}
