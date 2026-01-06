package com.mohithasan.journalapp.service;

import com.mohithasan.journalapp.dto.UserDTO;
import com.mohithasan.journalapp.entity.User;
import com.mohithasan.journalapp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        sampleUser = new User();
        sampleUser.setId(new ObjectId());
        sampleUser.setUserName("mohit");
        sampleUser.setEmail("mohit@test.com");
        sampleUser.setPassword("123456");
        sampleUser.setRoles(Collections.singletonList("USER"));
    }

    @Test
    void testSaveEntry_encodesPasswordAndSaves() {
        userService.saveEntry(sampleUser);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User savedUser = captor.getValue();
        assertNotEquals("123456", savedUser.getPassword());
        assertTrue(new BCryptPasswordEncoder().matches("123456", savedUser.getPassword()));
    }

    @Test
    void testSaveUser_callsRepository() {
        userService.saveUser(sampleUser);
        verify(userRepository, times(1)).save(sampleUser);
    }

    @Test
    void testSaveNewEntry_successful() {
        when(userRepository.existsByUserName("mohit")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDTO dto = new UserDTO();
        dto.setUserName("mohit");
        dto.setEmail("mohit@test.com");
        dto.setPassword("123456");

        User newUser = userService.saveNewEntry(dto);

        assertEquals("mohit", newUser.getUserName());
        assertEquals("mohit@test.com", newUser.getEmail());
        assertTrue(new BCryptPasswordEncoder().matches("123456", newUser.getPassword()));
        assertEquals(Collections.singletonList("USER"), newUser.getRoles());
    }

    @Test
    void testSaveNewEntry_usernameExists_throwsException() {
        when(userRepository.existsByUserName("mohit")).thenReturn(true);

        UserDTO dto = new UserDTO();
        dto.setUserName("mohit");

        assertThrows(IllegalArgumentException.class, () -> userService.saveNewEntry(dto));
    }

    @Test
    void testGetAll_returnsAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(sampleUser));

        List<User> users = userService.getAll();

        assertEquals(1, users.size());
        assertEquals("mohit", users.get(0).getUserName());
    }

    @Test
    void testFindById_returnsUser() {
        ObjectId id = sampleUser.getId();
        when(userRepository.findById(id)).thenReturn(Optional.of(sampleUser));

        Optional<User> result = userService.findById(id);
        assertTrue(result.isPresent());
        assertEquals("mohit", result.get().getUserName());
    }

    @Test
    void testDeleteByID_callsRepository() {
        ObjectId id = sampleUser.getId();
        userService.deleteByID(id);
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void testFindByUserName_returnsUser() {
        when(userRepository.findByUserName("mohit")).thenReturn(sampleUser);

        User result = userService.findByUserName("mohit");
        assertEquals("mohit", result.getUserName());
    }
}
