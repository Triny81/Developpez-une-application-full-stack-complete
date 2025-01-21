package com.openclassrooms.mddapi.controller;

import java.security.Principal;
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

import com.openclassrooms.mddapi.dto.ThemeDto;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.ThemeService;
import com.openclassrooms.mddapi.service.UserService;

@RestController
@RequestMapping("/api/themes")
public class ThemeController {

    @Autowired
    private ThemeService themeService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("{id}")
    public ResponseEntity<ThemeDto> getTheme(@PathVariable("id") final Long id) {
        Optional<Theme> theme = themeService.getTheme(id);
        if (theme.isPresent()) {
            return ResponseEntity.ok(convertToDto(theme.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping()
    public ResponseEntity<Map<String, ArrayList<ThemeDto>>> getThemes() {
        return ResponseEntity.ok(convertIterableToDto(themeService.getThemes()));
    }

    @PostMapping()
    public ResponseEntity<ThemeDto> createTheme(@RequestBody Theme theme) {
        return ResponseEntity.ok(convertToDto(themeService.saveTheme(theme)));
    }

    @PutMapping("{id}")
    public ResponseEntity<ThemeDto> updateTheme(@PathVariable("id") final Long id,
            @RequestBody Theme theme) {
        Optional<Theme> u = themeService.getTheme(id);

        if (u.isPresent()) {
            Theme currentTheme = u.get();

            String name = theme.getName();
            if (name != null) {
                currentTheme.setName(name);
            }

            String description = theme.getDescription();
            if (description != null) {
                currentTheme.setDescription(description);
            }

            themeService.saveTheme(currentTheme);

            return ResponseEntity.ok(convertToDto(currentTheme));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteTheme(@PathVariable("id") final Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("getUserSubscriptions") // get the subscriptions of the current user
    public ResponseEntity<?> getUserSubscriptions(Principal principal) {
        Optional<User> optUser = userService.getUserByEmail(principal.getName());

        if (optUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optUser.get();
        return ResponseEntity.ok(convertIterableToDto(user.getThemes()));
    }

    @GetMapping("updateSubscription/{themeId}") // subscribe or unsubscribe the current user
    public ResponseEntity<?> updateSubscription(@PathVariable("themeId") final Long themeId, Principal principal) {
        Optional<User> optUser = userService.getUserByEmail(principal.getName());
        Optional<Theme> optTheme = themeService.getTheme(themeId);

        if (optUser.isEmpty() || optTheme.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optUser.get();
        Theme theme = optTheme.get();

        boolean isSubscribed = userService.isSubscribedToTheme(user, theme);

        if (isSubscribed) {
            user.getThemes().remove(theme);
            userService.saveUser(user);
            return ResponseEntity.ok("{ \"message\": \"Unsubscribed to the theme " + theme.getName() + "\" }");
        } else {
            user.getThemes().add(theme);
            userService.saveUser(user);
            return ResponseEntity.ok("{ \"message\": \"Subscribed to the theme " + theme.getName() + "\" }");
        }
    }

    private ThemeDto convertToDto(Theme theme) {
        ThemeDto themeDTO = modelMapper.map(theme, ThemeDto.class);
        return themeDTO;
    }

    private Map<String, ArrayList<ThemeDto>> convertIterableToDto(Iterable<Theme> themes) {
        ArrayList<ThemeDto> themesDTO = new ArrayList<ThemeDto>();

        for (Theme u : themes) {
            ThemeDto themeDTO = modelMapper.map(u, ThemeDto.class);
            themesDTO.add(themeDTO);
        }

        Map<String, ArrayList<ThemeDto>> map = new HashMap<>();
        map.put("themes", themesDTO);

        return map;
    }
}
