package com.openclassrooms.mddapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.CommentRepository;

import lombok.Data;

@Data
@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;

    public Optional<Comment> getComment(final Long id) {
        return commentRepository.findById(id);
    }

    public List<Comment> getComments() {
        return commentRepository.findAll();
    }

    public Comment saveComment(Comment comment) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail((auth.getName())).get();

        comment.setAuthor(user);

        return commentRepository.save(comment);
    }

    public void deleteComment(final Long id) {
        commentRepository.deleteById(id);
    }

    public List<Comment> getCommentsByArticleId(Long articleId) {
        return commentRepository.findByArticleId(articleId);
    }
}
