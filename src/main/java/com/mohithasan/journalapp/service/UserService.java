package com.mohithasan.journalapp.service;

import com.mohithasan.journalapp.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import com.mohithasan.journalapp.entity.User;
import com.mohithasan.journalapp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void saveEntry(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void saveUser(User user){
        userRepository.save(user);
    }

    public User saveNewEntry(UserDTO user){
        if (userRepository.existsByUserName(user.getUserName())) {
            throw new IllegalArgumentException("Username already exists");
        }
        try {
            User newUser = new User();
            newUser.setUserName(user.getUserName());
            newUser.setEmail(user.getEmail());
            newUser.setPassword(passwordEncoder.encode(user.getPassword()));
            newUser.setRoles(Collections.singletonList("USER"));
            userRepository.save(newUser);
            return newUser;
        }catch (Exception e){
            log.error("There was an error while saving new user", e);
            throw new IllegalArgumentException("There was an error while saving new user");
        }
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public Optional<User> findById(ObjectId id) {
        return userRepository.findById(id);
    }

    public void deleteByID(ObjectId id){
        userRepository.deleteById(id);
    }

    public User findByUserName(String userName){
        return userRepository.findByUserName(userName);
    }

}
