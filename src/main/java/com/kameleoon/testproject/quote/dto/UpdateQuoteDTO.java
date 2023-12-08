package com.kameleoon.testproject.quote.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateQuoteDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String text;

    @JsonIgnore
    private String userEmail;
}
