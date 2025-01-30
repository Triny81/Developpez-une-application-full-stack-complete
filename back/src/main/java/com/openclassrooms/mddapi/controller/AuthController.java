package com.openclassrooms.mddapi.controller;

import java.security.Principal;
import java.time.Duration;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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

import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.payload.request.LoginRequest;
import com.openclassrooms.mddapi.payload.request.SignupRequest;
import com.openclassrooms.mddapi.service.JWTService;
import com.openclassrooms.mddapi.service.UserService;

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
    public ResponseEntity<?> login(@RequestBody LoginRequest login, HttpServletResponse response) {
        return authenticateUserAndSetCookie(login.getLogin(), login.getPassword(), response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        ResponseCookie deleteCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SignupRequest signup, HttpServletResponse response) throws Exception {
        String uncryptedPassword = signup.getPassword();

        // Regex pour chaque condition
        String regexDigit = ".*[0-9].*"; // Au moins un chiffre
        String regexLowercase = ".*[a-z].*"; // Au moins une lettre minuscule
        String regexUppercase = ".*[A-Z].*"; // Au moins une lettre majuscule
        String regexSpecialChar = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*"; // Au moins un caractère spécial

        // Vérifications
        boolean hasDigit = Pattern.matches(regexDigit, uncryptedPassword);
        boolean hasLowercase = Pattern.matches(regexLowercase, uncryptedPassword);
        boolean hasUppercase = Pattern.matches(regexUppercase, uncryptedPassword);
        boolean hasSpecialChar = Pattern.matches(regexSpecialChar, uncryptedPassword);

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

        if (!userService.getUserByUsername(signup.getUsername()).isEmpty()) {
            return ResponseEntity.badRequest().body("Username already taken");
        }

        if (!userService.getUserByEmail(signup.getEmail()).isEmpty()) {
            return ResponseEntity.badRequest().body("Email already taken");
        }

        User registeredUser = convertSignupRequestToUser(signup);
        registeredUser = userService.saveUser(registeredUser);

        if (registeredUser != null) {
            return authenticateUserAndSetCookie(signup.getEmail(), uncryptedPassword, response);
        } else {
            return ResponseEntity.badRequest().body("Registration failed");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        Optional<User> optUser = userService.getUserByEmail(principal.getName());

        if (optUser.isPresent()) {
            User user = optUser.get();
            return ResponseEntity.ok(convertToDto(user));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private ResponseEntity<?> authenticateUserAndSetCookie(String login, String password, HttpServletResponse response) {
        Optional<User> userOptional = userService.getUserByEmail(login);
        User user = userOptional.orElseGet(() -> userService.getUserByUsername(login).orElse(null));

        if (user == null) {
            return ResponseEntity.badRequest().body("Bad credentials");
        }

        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), password));
            String token = jwtService.generateToken(authentication);

            ResponseCookie jwtCookie = ResponseCookie.from("jwt", token)
                    .httpOnly(true)
                    .secure(true) 
                    .sameSite("Strict")
                    .path("/")
                    .maxAge(Duration.ofHours(24))
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

            return ResponseEntity.ok(convertToDto(user));

        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Bad credentials");
        }
    }

    private User convertSignupRequestToUser(SignupRequest signup) {
        User user = modelMapper.map(signup, User.class);
        return user;
    }

    private UserDto convertToDto(User user) {
        UserDto userDTO = modelMapper.map(user, UserDto.class);
        return userDTO;
    }
}
