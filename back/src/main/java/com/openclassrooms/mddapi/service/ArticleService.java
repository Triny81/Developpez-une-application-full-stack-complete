package com.openclassrooms.mddapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.ArticleRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserService userService;

    public Optional<Article> getArticle(final Long id) {
        return articleRepository.findById(id);
    }

    public List<Article> getArticles() {
        return articleRepository.findAll();
    }

    public Article saveArticle(Article article, final User user) {
        article.setAuthor(user);
        return articleRepository.save(article);
    }

    public void deleteArticle(final Long id) {
        articleRepository.deleteById(id);
    }

    public List<Article> getArticlesByUserSubscriptions(final Long userId) {
        return articleRepository.findArticlesByUserSubscriptions(userId);
    }
}
