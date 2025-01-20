package com.openclassrooms.mddapi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.service.CommentService;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("{id}")
    public ResponseEntity<CommentDto> getComment(@PathVariable("id") final Long id) {
        Optional<Comment> comment = commentService.getComment(id);
        if (comment.isPresent()) {
            return ResponseEntity.ok(convertToDto(comment.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping()
    public ResponseEntity<Map<String, ArrayList<CommentDto>>> getComments() {
        return ResponseEntity.ok(convertIterableToDto(commentService.getComments()));
    }

    @PostMapping()
    public ResponseEntity<CommentDto> createComment(@RequestBody Comment comment) {
        return ResponseEntity.ok(convertToDto(commentService.saveComment(comment)));
    }

    @PutMapping("{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable("id") final Long id,
            @RequestBody Comment comment) {
        Optional<Comment> u = commentService.getComment(id);

        if (u.isPresent()) {
            Comment currentComment = u.get();

            String message = comment.getMessage();
            if (message != null) {
                currentComment.setMessage(message);
            }

            Article article = comment.getArticle();
            if (article != null) {
                currentComment.setArticle(article);
            }

            commentService.saveComment(currentComment);

            return ResponseEntity.ok(convertToDto(currentComment));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") final Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/article/{articleId}")
    public ResponseEntity<Map<String, ArrayList<CommentDto>>> getCommentsByArticle(@PathVariable("articleId") Long articleId) {
        List<Comment> comments = commentService.getCommentsByArticleId(articleId);
        return ResponseEntity.ok(convertIterableToDto(comments));
    }

    private CommentDto convertToDto(Comment comment) {
        CommentDto commentDTO = modelMapper.map(comment, CommentDto.class);
        return commentDTO;
    }

    private Map<String, ArrayList<CommentDto>> convertIterableToDto(Iterable<Comment> comments) {
        ArrayList<CommentDto> commentsDTO = new ArrayList<CommentDto>();

        for (Comment u : comments) {
            CommentDto commentDTO = modelMapper.map(u, CommentDto.class);
            commentsDTO.add(commentDTO);
        }

        Map<String, ArrayList<CommentDto>> map = new HashMap<>();
        map.put("comments", commentsDTO);

        return map;
    }
}
