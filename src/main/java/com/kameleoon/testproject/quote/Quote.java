package com.kameleoon.testproject.quote;

import com.kameleoon.testproject.quote.dto.QuoteDTO;
import com.kameleoon.testproject.quote.dto.UpdateQuoteDTO;
import com.kameleoon.testproject.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
public class Quote {

    @Id
    @GeneratedValue
    private Long id;

    @Setter
    private String text;

    private LocalDate updateDate = LocalDate.now();

    @OneToOne
    private User user;

    public Quote() {}

    public Quote(String text, User user) {
        this.text = text;
        this.user = user;
    }

    public void update(UpdateQuoteDTO updateQuoteDTO) {
        text = updateQuoteDTO.getText();
        updateDate = LocalDate.now();
    }

    public QuoteDTO toDTO() {
        return QuoteDTO.builder()
                .id(id)
                .text(text)
                .publisher(user.getEmail())
                .lastUpdate(updateDate)
                .build();
    }
}
