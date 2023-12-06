package com.kameleoon.testproject.vote;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
class Id implements Serializable {

    private Long quoteId;

    private Long userId;

    public Id() {}

    public Id(Long quoteId, Long userId) {
        this.quoteId = quoteId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Id id)) return false;

        return Objects.equals(this.quoteId, id.quoteId) &&
                Objects.equals(this.userId, id.userId);
    }

    @Override
    public int hashCode() {
        return quoteId.hashCode() + userId.hashCode();
    }
}
