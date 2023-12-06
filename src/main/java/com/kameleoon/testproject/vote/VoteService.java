package com.kameleoon.testproject.vote;

import com.kameleoon.testproject.quote.Quote;
import com.kameleoon.testproject.user.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class VoteService {

    private final VoteRepository voteRepository;

    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public void addUpvote(Quote quote, User user) {
        voteRepository.save(new Vote(quote, user, VoteType.UPVOTE));
    }

    public void addDownvote(Quote quote, User user) {
        voteRepository.save(new Vote(quote, user, VoteType.DOWNVOTE));
    }
}
