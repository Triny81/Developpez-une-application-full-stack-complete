package com.openclassrooms.mddapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.configuration.SpringSecurityConfig;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;

import lombok.Data;

@Data
@Service
public class UserService {
    @Autowired
    private SpringSecurityConfig ssc;

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUser(final Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserByUsername(final String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(final Long id) {
        userRepository.deleteById(id);
    }

    public User saveUser(User user) { 
        if (user.getId() == null || !isPasswordEncoded(user.getPassword())) {
            user.setPassword(ssc.passwordEncoder().encode(user.getPassword()));
        }
    
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    private boolean isPasswordEncoded(String password) {
        return password != null && password.startsWith("$2");
    }
}
