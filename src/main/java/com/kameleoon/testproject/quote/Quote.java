package com.kameleoon.testproject.quote;

import com.kameleoon.testproject.quote.dto.QuoteDTO;
import com.kameleoon.testproject.quote.dto.UpdateQuoteDTO;
import com.kameleoon.testproject.user.User;
import com.kameleoon.testproject.vote.Vote;
import com.kameleoon.testproject.vote.VoteType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
public class Quote {

    @Id
    @GeneratedValue
    private Long id;

    @Setter
    private String text;

    private ZonedDateTime updateDate = ZonedDateTime.now();

    @ManyToOne(optional = false)
    private User publisher;

    @OneToMany(mappedBy = "quote", cascade = CascadeType.REMOVE)
    private Set<Vote> votes = new HashSet<>();

    public Quote() {}

    public Quote(String text, User publisher) {
        this.text = text;
        this.publisher = publisher;
    }

    public void update(UpdateQuoteDTO updateQuoteDTO) {
        text = updateQuoteDTO.getText();
        updateDate = ZonedDateTime.now();
    }

    public QuoteDTO toDTO() {
        long upvotes = getUpvotes(), downvotes = getDownvotes();

        return QuoteDTO.builder()
                .id(id).text(text)
                .publisher(publisher.getEmail())
                .lastUpdate(updateDate)
                .upvotes(upvotes)
                .downvotes(downvotes)
                .score(upvotes - downvotes)
                .build();
    }

    public long getUpvotes() {
        return votes.stream()
                .filter(v -> VoteType.UPVOTE.equals(v.getVoteType()))
                .count();
    }

    public long getDownvotes() {
        return votes.stream()
                .filter(v -> VoteType.DOWNVOTE.equals(v.getVoteType()))
                .count();
    }
}
