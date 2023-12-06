package com.kameleoon.testproject.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter @Setter
public class UserDTO {

    private Long id;

    private String name;

    private String email;

    private LocalDate creationDate;

}
