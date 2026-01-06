package com.mohithasan.journalapp.service;

import com.mohithasan.journalapp.dto.JournalEntryDTO;
import com.mohithasan.journalapp.entity.JournalEntry;
import com.mohithasan.journalapp.entity.User;
import com.mohithasan.journalapp.enums.Sentiment;
import com.mohithasan.journalapp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JournalEntryServiceTests {

    @Mock
    private JournalEntryRepository journalEntryRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private JournalEntryService journalEntryService;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setUserName("john");
        testUser.setJournalEntries(new ArrayList<>());
    }

    @Test
    void saveEntryDTO_ShouldSaveEntryAndReturnDTO() {
        JournalEntryDTO dto = new JournalEntryDTO();
        dto.setTitle("Test title");
        dto.setContent("Test content");
        dto.setSentiment(Sentiment.HAPPY);

        JournalEntry savedEntry = new JournalEntry();
        savedEntry.setId(new ObjectId());
        savedEntry.setTitle(dto.getTitle());
        savedEntry.setContent(dto.getContent());
        savedEntry.setSentiment(dto.getSentiment());
        savedEntry.setDate(LocalDateTime.now());

        when(userService.findByUserName("john")).thenReturn(testUser);
        when(journalEntryRepository.save(any(JournalEntry.class))).thenReturn(savedEntry);

        JournalEntryDTO result = journalEntryService.saveEntry(dto, "john");

        assertNotNull(result.getId());
        assertEquals(dto.getTitle(), result.getTitle());
        assertEquals(dto.getContent(), result.getContent());
        assertEquals(dto.getSentiment(), result.getSentiment());
        verify(journalEntryRepository, times(1)).save(any(JournalEntry.class));
        verify(userService, times(1)).saveUser(testUser);
    }

    @Test
    void saveEntryEntity_ShouldCallRepositorySave() {
        JournalEntry entry = new JournalEntry();
        entry.setTitle("Test");

        journalEntryService.saveEntry(entry);

        verify(journalEntryRepository, times(1)).save(entry);
    }

    @Test
    void getAll_ShouldReturnAllEntries() {
        List<JournalEntry> entries = List.of(new JournalEntry(), new JournalEntry());
        when(journalEntryRepository.findAll()).thenReturn(entries);

        List<JournalEntry> result = journalEntryService.getAll();

        assertEquals(2, result.size());
        verify(journalEntryRepository, times(1)).findAll();
    }

    @Test
    void findById_ShouldReturnEntryIfFound() {
        ObjectId id = new ObjectId();
        JournalEntry entry = new JournalEntry();
        entry.setId(id);

        when(journalEntryRepository.findById(id)).thenReturn(Optional.of(entry));

        Optional<JournalEntry> result = journalEntryService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }

    @Test
    void deleteByID_ShouldDeleteEntryIfExists() {
        ObjectId id = new ObjectId();
        JournalEntry entry = new JournalEntry();
        entry.setId(id);
        testUser.getJournalEntries().add(entry);

        when(userService.findByUserName("john")).thenReturn(testUser);

        boolean deleted = journalEntryService.deleteByID(id, "john");

        assertTrue(deleted);
        assertTrue(testUser.getJournalEntries().isEmpty());
        verify(journalEntryRepository, times(1)).deleteById(id);
        verify(userService, times(1)).saveUser(testUser);
    }

    @Test
    void deleteByID_ShouldReturnFalseIfEntryNotExists() {
        ObjectId id = new ObjectId();

        when(userService.findByUserName("john")).thenReturn(testUser);

        boolean deleted = journalEntryService.deleteByID(id, "john");

        assertFalse(deleted);
        verify(journalEntryRepository, never()).deleteById(any());
        verify(userService, never()).saveUser(any());
    }
}
