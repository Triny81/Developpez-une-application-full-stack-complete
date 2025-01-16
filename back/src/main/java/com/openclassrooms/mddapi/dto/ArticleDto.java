package com.openclassrooms.mddapi.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.model.User;

import lombok.Data;

@Data
public class ArticleDto {
    private Long id;

    private String title;

    private String message;

    private User author;

    private Theme theme;

    @JsonFormat(pattern="yyyy/MM/dd")
    private Date createdAt;

    @JsonFormat(pattern="yyyy/MM/dd")
    private Date updatedAt;
}
