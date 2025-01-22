package com.openclassrooms.mddapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.openclassrooms.mddapi.model.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("SELECT a FROM Article a WHERE a.theme.id IN (SELECT t.id FROM User u JOIN u.themes t WHERE u.id = :userId)")
    List<Article> findArticlesByUserSubscriptions(@Param("userId") Long userId);
}
