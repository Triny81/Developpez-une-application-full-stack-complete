package com.openclassrooms.mddapi.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class UserDto {
    private Long id;

    private String email;

    private String username;

    private boolean admin;

    @JsonFormat(pattern="yyyy/MM/dd")
    private Date createdAt;

    @JsonFormat(pattern="yyyy/MM/dd")
    private Date updatedAt;
}
