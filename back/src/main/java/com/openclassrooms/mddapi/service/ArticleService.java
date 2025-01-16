package com.openclassrooms.mddapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.repository.ArticleRepository;

import lombok.Data;

@Data
@Service
public class ArticleService {
@Autowired
    private ArticleRepository articleRepository;

    public Optional<Article> getArticle(final Long id) {
        return articleRepository.findById(id);
    }

    public List<Article> getArticles() {
        return articleRepository.findAll();
    }

    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    public void deleteArticle(final Long id) {
        articleRepository.deleteById(id);
    }
}
