package com.kameleoon.testproject.quote;

import com.kameleoon.testproject.vote.Vote;
import com.kameleoon.testproject.vote.VoteType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface QuoteRepository extends JpaRepository<Quote, Long> {

    @Query(
        "select q, sum(case " +
        "   when v.voteType is null then 0" +
        "   when v.voteType = 'UPVOTE' then 1" +
        "   when v.voteType = 'DOWNVOTE' then -1" +
        "end) as score " +
        "from Quote q " +
        "left join q.votes v " +
        "group by q.id"
    )
    List<Quote> getQuoteTop(PageRequest pageRequest);
}
