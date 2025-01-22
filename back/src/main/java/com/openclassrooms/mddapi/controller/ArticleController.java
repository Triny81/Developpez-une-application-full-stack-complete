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

import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.service.UserService;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("{id}")
    public ResponseEntity<ArticleDto> getArticle(@PathVariable("id") final Long id) {
        Optional<Article> article = articleService.getArticle(id);
        if (article.isPresent()) {
            return ResponseEntity.ok(convertToDto(article.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping()
    public ResponseEntity<Map<String, ArrayList<ArticleDto>>> getArticles() {
        return ResponseEntity.ok(convertIterableToDto(articleService.getArticles()));
    }

    @PostMapping()
    public ResponseEntity<ArticleDto> createArticle(@RequestBody Article article) {
        return ResponseEntity.ok(convertToDto(articleService.saveArticle(article)));
    }

    @PutMapping("{id}")
    public ResponseEntity<ArticleDto> updateArticle(@PathVariable("id") final Long id,
            @RequestBody Article article) {
        Optional<Article> u = articleService.getArticle(id);

        if (u.isPresent()) {
            Article currentArticle = u.get();

            String title = article.getTitle();
            if (title != null) {
                currentArticle.setTitle(title);
            }

            String message = article.getMessage();
            if (message != null) {
                currentArticle.setMessage(message);
            }

            Theme theme = article.getTheme();
            if (theme != null) {
                currentArticle.setTheme(theme);
            }

            articleService.saveArticle(currentArticle);

            return ResponseEntity.ok(convertToDto(currentArticle));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteArticle(@PathVariable("id") final Long id) {
        Optional<Article> article = articleService.getArticle(id);
        if (article.isPresent()) {
            articleService.deleteArticle(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("getUserSubscriptions") // get the articles of the subscriptions of the current user
    public ResponseEntity<?> getUserSubscriptions(Principal principal) {
        Optional<User> optUser = userService.getUserByEmail(principal.getName());

        if (optUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optUser.get();
        return ResponseEntity.ok(convertIterableToDto(articleService.getArticlesByUserSubscriptions(user.getId())));
    }

    private ArticleDto convertToDto(Article article) {
        ArticleDto articleDTO = modelMapper.map(article, ArticleDto.class);
        return articleDTO;
    }

    private Map<String, ArrayList<ArticleDto>> convertIterableToDto(Iterable<Article> articles) {
        ArrayList<ArticleDto> articlesDTO = new ArrayList<ArticleDto>();

        for (Article u : articles) {
            ArticleDto articleDTO = modelMapper.map(u, ArticleDto.class);
            articlesDTO.add(articleDTO);
        }

        Map<String, ArrayList<ArticleDto>> map = new HashMap<>();
        map.put("articles", articlesDTO);

        return map;
    }
}
