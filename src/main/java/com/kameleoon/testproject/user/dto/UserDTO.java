package com.kameleoon.testproject.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Builder
@Getter @Setter
public class UserDTO {

    private Long id;

    private String name;

    private String email;

    private ZonedDateTime creationDate;

}
