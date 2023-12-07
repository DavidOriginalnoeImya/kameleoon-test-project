package com.kameleoon.testproject.quote.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddQuoteDTO {

    private String text;

    @JsonIgnore
    private String publisherEmail;
}
