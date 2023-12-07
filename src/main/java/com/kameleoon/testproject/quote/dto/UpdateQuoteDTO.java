package com.kameleoon.testproject.quote.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateQuoteDTO {

    private Long id;

    private String text;

    @JsonIgnore
    private String userEmail;
}
