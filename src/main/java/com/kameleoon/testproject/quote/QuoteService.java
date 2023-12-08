package com.kameleoon.testproject.quote;

import com.kameleoon.testproject.quote.dto.AddQuoteDTO;
import com.kameleoon.testproject.quote.dto.QuoteDTO;
import com.kameleoon.testproject.quote.dto.UpdateQuoteDTO;
import com.kameleoon.testproject.quote.exceptions.QuoteDeletionDeniedException;
import com.kameleoon.testproject.quote.exceptions.QuoteNotFoundException;
import com.kameleoon.testproject.quote.exceptions.QuoteUpdateDeniedException;
import com.kameleoon.testproject.user.User;
import com.kameleoon.testproject.user.UserRepository;
import com.kameleoon.testproject.user.exceptions.UserNotFoundException;
import com.kameleoon.testproject.vote.VoteService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuoteService {

    private final QuoteRepository quoteRepository;

    private final UserRepository userRepository;

    private final VoteService voteService;

    public QuoteService(
            QuoteRepository quoteRepository,
            UserRepository userRepository,
            VoteService voteService
    ) {
        this.quoteRepository = quoteRepository;
        this.userRepository = userRepository;
        this.voteService = voteService;
    }

    public List<QuoteDTO> getQuotes() {
        return quoteRepository.findAll()
                .stream()
                .map(Quote::toDTO)
                .collect(Collectors.toList());
    }

    public List<QuoteDTO> getQuotes(int limit, QuoteSortDirection quoteSortDirection) {
        Sort.Direction sortDirection = quoteSortDirection.equals(QuoteSortDirection.TOP)
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        if (limit < 1) return List.of();

        return quoteRepository
                .getQuoteTop(PageRequest.of(0, limit, sortDirection, "score"))
                .stream().map(Quote::toDTO)
                .collect(Collectors.toList());
    }

    public QuoteDTO getQuote(Long id) {
        return findQuoteById(id).toDTO();
    }

    public QuoteDTO getRandomQuote() {
        List<Quote> quotes = quoteRepository.findAll();

        if (!quotes.isEmpty()) {
            return quotes
                    .get(ThreadLocalRandom.current().nextInt(0, quotes.size()))
                    .toDTO();
        }

        throw new QuoteNotFoundException("Quotes not found");
    }

    public QuoteDTO addQuote(AddQuoteDTO addQuoteDTO) {
        User user = findUserByEmail(addQuoteDTO.getPublisherEmail());
        Quote quote = new Quote(addQuoteDTO.getText(), user);
        return quoteRepository.save(quote).toDTO();
    }

    public QuoteDTO updateQuote(UpdateQuoteDTO updateQuoteDTO) {
        Quote quote = findQuoteById(updateQuoteDTO.getId());

        if (userEmailEquals(quote.getPublisher(), updateQuoteDTO.getUserEmail())) {
            quote.update(updateQuoteDTO);
            return quoteRepository.save(quote).toDTO();
        }

        throw new QuoteUpdateDeniedException(String.format(
            "User %s doesn't have permission to edit this quote",
            updateQuoteDTO.getUserEmail()
        ));
    }

    public void upvoteQuote(Long id, String userEmail) {
        Quote quote = findQuoteById(id);
        User user = findUserByEmail(userEmail);
        voteService.addUpvote(quote, user);
    }

    public void downvoteQuote(Long id, String userEmail) {
        Quote quote = findQuoteById(id);
        User user = findUserByEmail(userEmail);
        voteService.addDownvote(quote, user);
    }

    public void deleteQuote(Long id, String userEmail) {
        Quote quote = findQuoteById(id);

        if (userEmailEquals(quote.getPublisher(), userEmail)) {
            quoteRepository.delete(quote);
        }
        else {
            throw new QuoteDeletionDeniedException(String.format(
                "User %s doesn't have permission to delete this quote", userEmail
            ));
        }
    }

    private Quote findQuoteById(Long id) {
        return quoteRepository.findById(id)
                .orElseThrow(() -> new QuoteNotFoundException(
                        String.format("Quote with id = %d not found", id)
                ));
    }

    public User findUserByEmail(String email) {
        return userRepository
                .getByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User with email %s not found", email)
                ));
    }
    
    private boolean userEmailEquals(User user, String email) {
        return Objects.equals(user.getEmail(), email);
    }
}
