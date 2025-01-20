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

import com.openclassrooms.mddapi.dto.ThemeDto;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.service.ThemeService;

@RestController
@RequestMapping("/api/themes")
public class ThemeController {

    @Autowired
    private ThemeService themeService;

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
