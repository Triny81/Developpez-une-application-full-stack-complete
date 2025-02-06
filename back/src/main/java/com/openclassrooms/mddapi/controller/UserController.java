package com.openclassrooms.mddapi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") final Long id) {
        Optional<User> user = userService.getUser(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(convertToDto(user.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping()
    public ResponseEntity<Map<String, ArrayList<UserDto>>> getUsers() {
        return ResponseEntity.ok(convertIterableToDto(userService.getUsers()));
    }

    @PostMapping()
    public ResponseEntity<UserDto> createUser(@RequestBody User user) {
        return ResponseEntity.ok(convertToDto(userService.saveUser(user)));
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") final Long id,
            @RequestBody User user) {
        Optional<User> u = userService.getUser(id);

        if (u.isPresent()) {
            User currentUser = u.get();

            String email = user.getEmail();
            if (email != null) {
                if (!email.equals(currentUser.getEmail()) && userService.getUserByEmail(user.getEmail()).isPresent()) {
                    return ResponseEntity.badRequest().body("Email already taken");
                }

                currentUser.setEmail(email);
            }

            String username = user.getUsername();
            if (username != null) {
                if (!username.equals(currentUser.getUsername())
                        && userService.getUserByUsername(user.getUsername()).isPresent()) {
                    return ResponseEntity.badRequest().body("Username already taken");
                }

                currentUser.setUsername(username);
            }

            String uncryptedPassoword = user.getPassword();
            if (uncryptedPassoword != null) {
                // Regex pour chaque condition
                String regexDigit = ".*[0-9].*"; // Au moins un chiffre
                String regexLowercase = ".*[a-z].*"; // Au moins une lettre minuscule
                String regexUppercase = ".*[A-Z].*"; // Au moins une lettre majuscule
                String regexSpecialChar = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*"; // Au moins un caractère
                                                                                              // spécial
                // Vérifications
                boolean hasDigit = Pattern.matches(regexDigit, uncryptedPassoword);
                boolean hasLowercase = Pattern.matches(regexLowercase, uncryptedPassoword);
                boolean hasUppercase = Pattern.matches(regexUppercase, uncryptedPassoword);
                boolean hasSpecialChar = Pattern.matches(regexSpecialChar, uncryptedPassoword);

                if (!hasDigit) {
                    return ResponseEntity.badRequest().body("Password must contain at least one number");
                }

                if (!hasLowercase) {
                    return ResponseEntity.badRequest().body("Password must contain at least one lowercase letter");
                }

                if (!hasUppercase) {
                    return ResponseEntity.badRequest().body("Password must contain at least one capital letter");
                }

                if (!hasSpecialChar) {
                    return ResponseEntity.badRequest().body("Password must contain at least one special character");
                }

                currentUser.setPassword(uncryptedPassoword);
            }

            userService.saveUser(currentUser);

            return ResponseEntity.ok(convertToDto(currentUser));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") final Long id) {
        Optional<User> user = userService.getUser(id);
        if (user.isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private UserDto convertToDto(User user) {
        UserDto userDTO = modelMapper.map(user, UserDto.class);
        return userDTO;
    }

    private Map<String, ArrayList<UserDto>> convertIterableToDto(Iterable<User> users) {
        ArrayList<UserDto> usersDTO = new ArrayList<UserDto>();

        for (User u : users) {
            UserDto userDTO = modelMapper.map(u, UserDto.class);
            usersDTO.add(userDTO);
        }

        Map<String, ArrayList<UserDto>> map = new HashMap<>();
        map.put("users", usersDTO);

        return map;
    }
}
