package com.kameleoon.testproject.vote;

import com.kameleoon.testproject.quote.Quote;
import com.kameleoon.testproject.user.User;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

@Entity
public class Vote {

    @EmbeddedId
    private Id id;

    @ManyToOne
    @JoinColumn(
        name = Id_.QUOTE_ID,
        insertable=false,
        updatable=false
    )
    private Quote quote;

    @ManyToOne
    @JoinColumn(
        name = Id_.USER_ID,
        insertable=false,
        updatable=false
    )
    private User user;

    private boolean upvote;

    private LocalDate voteDate = LocalDate.now();

    protected Vote() {}

    public Vote(Quote quote, User user, boolean upvote) {
        this.id = new Id(quote.getId(), user.getId());
        this.quote = quote;
        this.user = user;
        this.upvote = upvote;
    }
}
