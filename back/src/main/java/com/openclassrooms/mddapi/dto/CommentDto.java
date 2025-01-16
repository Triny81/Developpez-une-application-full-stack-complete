package com.openclassrooms.mddapi.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.User;

import lombok.Data;

@Data
public class CommentDto {
    private Long id;

    private String message;

    private User author;

    private Article article;

    @JsonFormat(pattern="yyyy/MM/dd")
    private Date createdAt;

    @JsonFormat(pattern="yyyy/MM/dd")
    private Date updatedAt;
}
