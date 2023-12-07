package com.kameleoon.testproject.quote.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Builder
@Getter @Setter
public class QuoteDTO {

    private Long id;

    private String text;

    private ZonedDateTime lastUpdate;

    private String publisher;

    private long upvotes;

    private long downvotes;

    private long score;
}
