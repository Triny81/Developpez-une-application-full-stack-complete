package com.openclassrooms.mddapi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

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
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") final Long id,
            @RequestBody User user) {
        Optional<User> u = userService.getUser(id);

        if (u.isPresent()) {
            User currentUser = u.get();

            String email = user.getEmail();
            if (email != null) {
                currentUser.setEmail(email);
            }

            String username = user.getUsername();
            if (username != null) {
                currentUser.setUsername(username);
            }

            String password = user.getPassword();
            if (password != null) {
                currentUser.setPassword(password);
            }

            userService.saveUser(currentUser);

            return ResponseEntity.ok(convertToDto(currentUser));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") final Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
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
