package com.kameleoon.testproject.quote.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter @Setter
public class QuoteDTO {

    private Long id;

    private String text;

    private LocalDate lastUpdate;

    private String publisher;
}
