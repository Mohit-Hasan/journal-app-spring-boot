package com.mohithasan.journalapp.service;

import com.mohithasan.journalapp.entity.User;
import com.mohithasan.journalapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceAuth implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceAuth(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);
        if(user != null){
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUserName())
                    .password(user.getPassword())
                    .roles(user.getRoles().toArray(new String[0]))
                    .build();
        }
        throw new UsernameNotFoundException("User not found with username: "+ username);
    }

    public boolean isExistByUsername(String username) {
        return userRepository.findByUserName(username) != null;
    }

}
