package com.kameleoon.testproject.quote.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddQuoteDTO {

    @NotBlank
    private String text;

    @JsonIgnore
    private String publisherEmail;
}
