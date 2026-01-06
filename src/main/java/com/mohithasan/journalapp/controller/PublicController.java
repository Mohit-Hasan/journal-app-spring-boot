package com.mohithasan.journalapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import com.mohithasan.journalapp.dto.UserDTO;
import com.mohithasan.journalapp.entity.User;
import com.mohithasan.journalapp.service.UserDetailsServiceAuth;
import com.mohithasan.journalapp.service.UserService;
import com.mohithasan.journalapp.utilis.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/public")
@Tag(name = "Public APIs")
public class PublicController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceAuth userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Autowired
    public PublicController(
            AuthenticationManager authenticationManager,
            UserDetailsServiceAuth userDetailsService,
            JwtUtil jwtUtil,
            UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @GetMapping("/health-check")
    public String healthCheck(){
        return "ok";
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@Valid @RequestBody UserDTO user){
        try{
            User newUser = userService.saveNewEntry(user);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        }catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(Map.of("error", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody UserDTO user){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(Map.of(
                    "error", "Incorrect username or password"), HttpStatus.BAD_REQUEST);
        }
    }

}
