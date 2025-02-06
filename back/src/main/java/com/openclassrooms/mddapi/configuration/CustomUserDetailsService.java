package com.openclassrooms.mddapi.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<com.openclassrooms.mddapi.model.User> userOptionnal = userRepository.findByEmail(login);
        com.openclassrooms.mddapi.model.User user;

        if (userOptionnal.isEmpty()) { // try with username if email not found
            userOptionnal = userRepository.findByUsername(login);

            if (!userOptionnal.isEmpty()) {
                user = userOptionnal.get();
                return new User(user.getEmail(), user.getPassword(), getGrantedAuthorities("USER"));
    
            } else {
                throw new UsernameNotFoundException("Username or email not found");
            }
        }

        user = userOptionnal.get();
        return new User(user.getEmail(), user.getPassword(), getGrantedAuthorities("USER"));
    }

    private List<GrantedAuthority> getGrantedAuthorities(String role) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        return authorities;
    }
}
