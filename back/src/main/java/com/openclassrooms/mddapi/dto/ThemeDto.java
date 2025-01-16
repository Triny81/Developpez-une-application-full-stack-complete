package com.openclassrooms.mddapi.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ThemeDto {
    private Long id;

    private String name;

    private String description;

    @JsonFormat(pattern="yyyy/MM/dd")
    private Date createdAt;

    @JsonFormat(pattern="yyyy/MM/dd")
    private Date updatedAt;
}
