package com.mohithasan.journalapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import com.mohithasan.journalapp.cache.AppCache;
import com.mohithasan.journalapp.entity.User;
import com.mohithasan.journalapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin APIs")
public class AdminController {

    private final UserService userService;
    private final AppCache appCache;

    @Autowired
    public AdminController(UserService userService, AppCache appCache){
        this.userService = userService;
        this.appCache = appCache;
    }

    @GetMapping("/all-users")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> all = userService.getAll();
        if(all != null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/clear-cache")
    public ResponseEntity<Void> clearCache(){
        appCache.inti();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
