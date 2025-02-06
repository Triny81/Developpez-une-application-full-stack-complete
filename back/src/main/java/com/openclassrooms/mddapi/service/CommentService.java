package com.openclassrooms.mddapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.CommentRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;

    public Optional<Comment> getComment(final Long id) {
        return commentRepository.findById(id);
    }

    public List<Comment> getComments() {
        return commentRepository.findAll();
    }

    public Comment saveComment(Comment comment, final User user) {
        comment.setAuthor(user);
        return commentRepository.save(comment);
    }

    public void deleteComment(final Long id) {
        commentRepository.deleteById(id);
    }

    public List<Comment> getCommentsByArticleId(final Long articleId) {
        return commentRepository.findByArticleId(articleId);
    }
}
