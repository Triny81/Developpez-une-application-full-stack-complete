package com.openclassrooms.mddapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.repository.ThemeRepository;

import lombok.Data;

@Data
@Service
public class ThemeService {
    @Autowired
    private ThemeRepository themeRepository;

    public Optional<Theme> getTheme(final Long id) {
        return themeRepository.findById(id);
    }

    public List<Theme> getThemes() {
        return themeRepository.findAll();
    }

    public Theme saveTheme(Theme theme) {
        return themeRepository.save(theme);
    }

    public void deleteTheme(final Long id) {
        themeRepository.deleteById(id);
    }
}
