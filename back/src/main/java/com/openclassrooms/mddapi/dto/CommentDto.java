package com.openclassrooms.mddapi.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class CommentDto {
    private Long id;

    private String message;

    private UserDto author;

    private ArticleDto article;

    @JsonFormat(pattern="yyyy/MM/dd")
    private Date createdAt;

    @JsonFormat(pattern="yyyy/MM/dd")
    private Date updatedAt;
}
