package com.mohithasan.journalapp.controller;


import com.mohithasan.journalapp.dto.JournalEntryDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.mohithasan.journalapp.entity.JournalEntry;
import com.mohithasan.journalapp.entity.User;
import com.mohithasan.journalapp.service.JournalEntryService;
import com.mohithasan.journalapp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("journal")
@Tag(name = "Journal APIs")
public class JournalEntryController {

    private final JournalEntryService journalEntryService;
    private final UserService userService;

    @Autowired
    JournalEntryController(JournalEntryService journalEntryService, UserService userService){
        this.journalEntryService = journalEntryService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAllJournalEntriesOfUser(){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            User user = userService.findByUserName(userName);
            List<JournalEntry> all = user.getJournalEntries();
            if(all != null && !all.isEmpty()){
                return  new ResponseEntity<>(all, HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<JournalEntryDTO> createEntry(@Valid @RequestBody JournalEntryDTO myEntry){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            JournalEntryDTO saved = journalEntryService.saveEntry(myEntry, userName);

            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getJournalById(@PathVariable String myId){
        try {
            ObjectId objectId = new ObjectId(myId);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            User user = userService.findByUserName(userName);
            List<JournalEntry> collect = user.getJournalEntries().stream().filter(x-> x.getId().equals(objectId)).collect(Collectors.toList());
            if(!collect.isEmpty()){
                Optional<JournalEntry> journalEntry = journalEntryService.findById(objectId);
                if(journalEntry.isPresent()){
                    return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
                }
            }
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{myId}")
    public ResponseEntity<Void> deleteJournalEntryById(@PathVariable ObjectId myId){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            boolean removed = journalEntryService.deleteByID(myId, userName);
            if(removed){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/id/{id}")
    public ResponseEntity<JournalEntry> updateUser(
            @PathVariable ObjectId id,
            @Valid @RequestBody JournalEntryDTO newEntry
    ) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            User user = userService.findByUserName(userName);
            List<JournalEntry> collect = user.getJournalEntries().stream().filter(x-> x.getId().equals(id)).collect(Collectors.toList());
            if(!collect.isEmpty()){
                JournalEntry old = journalEntryService.findById(id).orElse(null);
                if(old != null){
                    old.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().isEmpty() ? newEntry.getTitle() : old.getTitle());
                    old.setContent(newEntry.getContent() != null && !newEntry.getContent().isEmpty() ? newEntry.getContent() : old.getContent());
                    old.setSentiment(newEntry.getSentiment() != null ? newEntry.getSentiment() : old.getSentiment());
                    journalEntryService.saveEntry(old);
                    return new ResponseEntity<>(old, HttpStatus.OK);
                }
            }
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
