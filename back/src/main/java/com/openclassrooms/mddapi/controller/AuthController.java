package com.openclassrooms.mddapi.controller;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.payload.request.LoginRequest;
import com.openclassrooms.mddapi.payload.request.SignupRequest;
import com.openclassrooms.mddapi.service.JWTService;
import com.openclassrooms.mddapi.service.UserService;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    private JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(JWTService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest login) {
        return getTokenJSON(login.getLogin(), login.getPassword());
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody SignupRequest signup) throws Exception {

        String uncryptedPassoword = signup.getPassword();
        User registeredUser = convertSignupRequestToUser(signup);
        registeredUser = userService.saveUser(registeredUser);

        if (registeredUser != null) {
            return getTokenJSON(signup.getEmail(), uncryptedPassoword);
        } else {
            return ResponseEntity.badRequest().body("Registration failed");
        }
    }

    private ResponseEntity<String> getTokenJSON(String login, String encryptedPassword) { // return token in a JSON
        Optional<User> userOptionnal = userService.getUserByEmail(login);
        User user;

        if (userOptionnal.isEmpty()) { // try with username if email not found
            userOptionnal = userService.getUserByUsername(login);

            if (userOptionnal.isEmpty()) {
                return ResponseEntity.badRequest().body("Bad credentials");
            } else {
                user = userOptionnal.get();
            }

        } else {
            user = userOptionnal.get();
        }

        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), encryptedPassword));
            String token = jwtService.generateToken(authentication);

            return ResponseEntity.ok("{ \"token\": \"" + token + "\" }");

        } catch (BadCredentialsException e) { // email / username not found
            return ResponseEntity.badRequest().body("Bad credentials");
        } catch (Exception e) { // error password
            return ResponseEntity.badRequest().body("Bad credentials");
        }
    }

    private User convertSignupRequestToUser(SignupRequest signup) {
		User user = modelMapper.map(signup, User.class);
		return user;
	}
}
