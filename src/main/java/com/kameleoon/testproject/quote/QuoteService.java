package com.kameleoon.testproject.quote;

import com.kameleoon.testproject.quote.dto.AddQuoteDTO;
import com.kameleoon.testproject.quote.dto.QuoteDTO;
import com.kameleoon.testproject.quote.dto.UpdateQuoteDTO;
import com.kameleoon.testproject.user.User;
import com.kameleoon.testproject.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuoteService {

    private final QuoteRepository quoteRepository;

    private final UserRepository userRepository;

    public QuoteService(QuoteRepository quoteRepository, UserRepository userRepository) {
        this.quoteRepository = quoteRepository;
        this.userRepository = userRepository;
    }

    public List<QuoteDTO> getQuotes() {
        return quoteRepository.findAll()
                .stream()
                .map(Quote::toDTO)
                .collect(Collectors.toList());
    }

    public QuoteDTO getQuote(Long id) {
        return quoteRepository.findById(id)
                .orElseThrow()
                .toDTO();
    }

    public QuoteDTO getRandomQuote() {
        List<Quote> quotes = quoteRepository.findAll();
        return quotes
                .get(ThreadLocalRandom.current().nextInt(0, quotes.size()))
                .toDTO();
    }

    public QuoteDTO addQuote(AddQuoteDTO addQuoteDTO) {
        User user = userRepository
                .getByEmail(addQuoteDTO.getPublisherEmail())
                .orElseThrow();

        Quote quote = new Quote(addQuoteDTO.getText(), user);
        return quoteRepository.save(quote).toDTO();
    }

    public QuoteDTO updateQuote(UpdateQuoteDTO updateQuoteDTO) {
        Quote quote = quoteRepository
                .findById(updateQuoteDTO.getId())
                .orElseThrow();

        quote.update(updateQuoteDTO);
        return quoteRepository.save(quote).toDTO();
    }

    public void deleteQuote(Long id) {
        Quote quote = quoteRepository.findById(id).orElseThrow();
        quoteRepository.delete(quote);
    }
}
