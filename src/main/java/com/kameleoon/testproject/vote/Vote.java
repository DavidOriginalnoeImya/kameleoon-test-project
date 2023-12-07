package com.kameleoon.testproject.vote;

import com.kameleoon.testproject.quote.Quote;
import com.kameleoon.testproject.user.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
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

    @Enumerated(EnumType.STRING)
    private VoteType voteType;

    protected Vote() {}

    public Vote(Quote quote, User user, VoteType voteType) {
        this.id = new Id(quote.getId(), user.getId());
        this.quote = quote;
        this.user = user;
        this.voteType = voteType;
    }
}
