package com.mohithasan.journalapp.controller;

import com.mohithasan.journalapp.dto.JournalEntryDTO;
import com.mohithasan.journalapp.entity.JournalEntry;
import com.mohithasan.journalapp.entity.User;
import com.mohithasan.journalapp.enums.Sentiment;
import com.mohithasan.journalapp.service.JournalEntryService;
import com.mohithasan.journalapp.service.UserService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JournalEntryControllerTests {

    @Mock
    private JournalEntryService journalEntryService;

    @Mock
    private UserService userService;

    @InjectMocks
    private JournalEntryController journalEntryController;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        mockUser = new User();
        mockUser.setUserName("testuser");

        JournalEntry entry = new JournalEntry();
        entry.setId(new ObjectId());
        entry.setTitle("Test Title");
        entry.setContent("Test Content");
        entry.setSentiment(Sentiment.HAPPY);
        entry.setDate(LocalDateTime.now());

        mockUser.setJournalEntries(Collections.singletonList(entry));

        when(authentication.getName()).thenReturn("testuser");
        when(userService.findByUserName("testuser")).thenReturn(mockUser);
    }

    @Test
    void testGetAllJournalEntriesOfUser_ReturnsEntries() {
        ResponseEntity<List<JournalEntry>> response = journalEntryController.getAllJournalEntriesOfUser();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Test Title", response.getBody().get(0).getTitle());
    }

    @Test
    void testGetAllJournalEntriesOfUser_ReturnsNotFound() {
        mockUser.setJournalEntries(Collections.emptyList());
        ResponseEntity<List<JournalEntry>> response = journalEntryController.getAllJournalEntriesOfUser();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateEntry_ReturnsCreated() {
        JournalEntryDTO dto = new JournalEntryDTO();
        dto.setTitle("New Entry");
        dto.setContent("New Content");
        dto.setSentiment(Sentiment.HAPPY);

        when(journalEntryService.saveEntry(dto, "testuser")).thenReturn(dto);

        ResponseEntity<JournalEntryDTO> response = journalEntryController.createEntry(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void testGetJournalById_ReturnsEntry() {
        JournalEntry existingEntry = mockUser.getJournalEntries().get(0);
        ObjectId id = existingEntry.getId();

        when(journalEntryService.findById(id)).thenReturn(Optional.of(existingEntry));

        ResponseEntity<JournalEntry> response = journalEntryController.getJournalById(id.toHexString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(existingEntry.getTitle(), response.getBody().getTitle());
    }

    @Test
    void testDeleteJournalEntryById_ReturnsNoContent() {
        ObjectId id = mockUser.getJournalEntries().get(0).getId();
        when(journalEntryService.deleteByID(id, "testuser")).thenReturn(true);

        ResponseEntity<Void> response = journalEntryController.deleteJournalEntryById(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testUpdateUser_ReturnsUpdatedEntry() {
        JournalEntryDTO dto = new JournalEntryDTO();
        dto.setTitle("Updated Title");
        dto.setContent("Updated Content");
        dto.setSentiment(Sentiment.SAD);

        JournalEntry existingEntry = mockUser.getJournalEntries().get(0);
        ObjectId id = existingEntry.getId();

        when(journalEntryService.findById(id)).thenReturn(Optional.of(existingEntry));

        ResponseEntity<JournalEntry> response = journalEntryController.updateUser(id, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Title", response.getBody().getTitle());
        assertEquals("Updated Content", response.getBody().getContent());
        assertEquals(Sentiment.SAD, response.getBody().getSentiment());
    }
}
