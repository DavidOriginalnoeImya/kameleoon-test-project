package com.kameleoon.testproject.user.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public class UserDTO {

    private Long id;

    private String name;

    private String email;

    private LocalDate creationDate;

}
