package com.mohithasan.journalapp.controller;


import com.mohithasan.journalapp.dto.UserDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.mohithasan.journalapp.api.response.WeatherResponse;
import com.mohithasan.journalapp.entity.User;
import com.mohithasan.journalapp.repository.UserRepository;
import com.mohithasan.journalapp.service.UserService;
import com.mohithasan.journalapp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@Tag(name = "User APIs")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final WeatherService weatherService;

    @Autowired
    public UserController(
            UserService userService,
            UserRepository userRepository,
            WeatherService weatherService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.weatherService = weatherService;
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody UserDTO user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User userInDb = userService.findByUserName(username);
        try{
            if(userInDb != null){
                userInDb.setUserName(user.getUserName() != null && !user.getUserName().isEmpty() ? user.getUserName() : userInDb.getUserName());
                userInDb.setPassword(user.getPassword() != null && !user.getPassword().isEmpty() ? user.getPassword() : userInDb.getPassword());
                userInDb.setEmail(user.getEmail() != null && !user.getEmail().isEmpty() ? user.getEmail() : userInDb.getEmail());
                userService.saveEntry(userInDb);
                return new ResponseEntity<>(userInDb, HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUserById(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUserName(username);
        userService.deleteByID(user.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<Object> greeting(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        WeatherResponse weatherResponse = weatherService.getWeather("Khulna");
        String greeting = "";
        if(weatherResponse != null){
            greeting = ", Weather Fees Like: " + weatherResponse.getCurrent().getFeelsLike();
        }
        return new ResponseEntity<>("Hi " + username + greeting, HttpStatus.OK);
    }
}
