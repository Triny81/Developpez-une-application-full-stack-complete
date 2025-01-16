package com.openclassrooms.mddapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.repository.CommentRepository;

import lombok.Data;

@Data
@Service
public class CommentService {
@Autowired
    private CommentRepository commentRepository;

    public Optional<Comment> getComment(final Long id) {
        return commentRepository.findById(id);
    }

    public List<Comment> getComments() {
        return commentRepository.findAll();
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public void deleteComment(final Long id) {
        commentRepository.deleteById(id);
    }
}
