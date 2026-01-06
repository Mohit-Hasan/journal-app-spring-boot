package com.mohithasan.journalapp.service;

import com.mohithasan.journalapp.dto.JournalEntryDTO;
import com.mohithasan.journalapp.entity.JournalEntry;
import com.mohithasan.journalapp.entity.User;
import com.mohithasan.journalapp.repository.JournalEntryRepository;
import com.mongodb.MongoException;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;
    private final UserService userService;

    @Autowired
    public JournalEntryService(JournalEntryRepository journalEntryRepository, UserService userService){
        this.journalEntryRepository = journalEntryRepository;
        this.userService = userService;
    }

    private static final Logger logger = LoggerFactory.getLogger(JournalEntryService.class);

    @Transactional
    public JournalEntryDTO saveEntry(JournalEntryDTO journalEntryDTO, String userName){
        try {
            User user = userService.findByUserName(userName);

            JournalEntry journalEntry = new JournalEntry();
            journalEntry.setTitle(journalEntryDTO.getTitle());
            journalEntry.setContent(journalEntryDTO.getContent());
            journalEntry.setSentiment(journalEntryDTO.getSentiment());
            journalEntry.setDate(LocalDateTime.now());

            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveUser(user);

            journalEntryDTO.setId(saved.getId());
            journalEntryDTO.setDate(saved.getDate());

            return journalEntryDTO;
        }catch (Exception e){
            logger.error("An error occurred while saving journal entry.", e);
            throw new RuntimeException("An error occurred while saving journal entry.", e);
        }

    }

    public void saveEntry(JournalEntry journalEntry){
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll(){
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteByID(ObjectId id, String userName){
        boolean removed = false;
        try {
            User user = userService.findByUserName(userName);
            removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if(removed){
                journalEntryRepository.deleteById(id);
                userService.saveUser(user);
            }
        }catch (Exception e){
            throw new MongoException("An error occurred while saving the  entry.", e);
        }
        return removed;
    }

}
