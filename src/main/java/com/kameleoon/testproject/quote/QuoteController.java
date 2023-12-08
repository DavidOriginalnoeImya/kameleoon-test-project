package com.kameleoon.testproject.quote;

import com.kameleoon.testproject.quote.dto.AddQuoteDTO;
import com.kameleoon.testproject.quote.dto.QuoteDTO;
import com.kameleoon.testproject.quote.dto.UpdateQuoteDTO;
import com.kameleoon.testproject.quote.exceptions.QuoteDeletionDeniedException;
import com.kameleoon.testproject.quote.exceptions.QuoteNotFoundException;
import com.kameleoon.testproject.quote.exceptions.QuoteUpdateDeniedException;
import com.kameleoon.testproject.user.exceptions.UserNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quotes")
public class QuoteController {

    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @GetMapping
    public ResponseEntity<List<QuoteDTO>> getQuotes() {
        return ResponseEntity
                .ok(quoteService.getQuotes());
    }

    @GetMapping("/top")
    public ResponseEntity<List<QuoteDTO>> getQuoteTop(@RequestParam Integer limit) {
        return ResponseEntity
                .ok(quoteService.getQuotes(limit, QuoteSortDirection.TOP));
    }

    @GetMapping("/flop")
    public ResponseEntity<List<QuoteDTO>> getQuoteFlop(@RequestParam Integer limit) {
        return ResponseEntity
                .ok(quoteService.getQuotes(limit, QuoteSortDirection.FLOP));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuoteDTO> getQuote(@PathVariable Long id) {
        return ResponseEntity
                .ok(quoteService.getQuote(id));
    }

    @PostMapping
    public ResponseEntity<QuoteDTO> addQuote(
        @RequestBody AddQuoteDTO addQuoteDTO,
        @RequestHeader(name = "Email", defaultValue = "test@mail.com") String userEmail
    ) {
        addQuoteDTO.setPublisherEmail(userEmail);
        QuoteDTO quoteDTO = quoteService.addQuote(addQuoteDTO);
        return ResponseEntity
                .created(
                    ServletUriComponentsBuilder
                            .fromCurrentRequestUri()
                            .pathSegment(String.valueOf(quoteDTO.getId()))
                            .build().toUri()
                )
                .body(quoteDTO);
    }

    @PostMapping("/{id}/upvote")
    public ResponseEntity<?> upvoteQuote(
            @PathVariable Long id,
            @RequestHeader(name = "Email", defaultValue = "test@mail.com") String userEmail
    ) {
        quoteService.upvoteQuote(id, userEmail);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PostMapping("/{id}/downvote")
    public ResponseEntity<?> downvoteQuote(
            @PathVariable Long id,
            @RequestHeader(name = "Email", defaultValue = "test@mail.com") String userEmail
    ) {
        quoteService.downvoteQuote(id, userEmail);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PutMapping
    public ResponseEntity<QuoteDTO> updateQuote(
        @RequestBody UpdateQuoteDTO updateQuoteDTO,
        @RequestHeader(name = "Email", defaultValue = "test@mail.com") String userEmail
    ) {
        updateQuoteDTO.setUserEmail(userEmail);
        return ResponseEntity
                .ok(quoteService.updateQuote(updateQuoteDTO));
    }

    @GetMapping("/random")
    public ResponseEntity<QuoteDTO> getRandomQuote() {
        return ResponseEntity
                .ok(quoteService.getRandomQuote());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuote(
        @PathVariable Long id,
        @RequestHeader(name = "Email", defaultValue = "test@mail.com") String userEmail
    ) {
        quoteService.deleteQuote(id, userEmail);
        return ResponseEntity
                .noContent()
                .build();
    }

    @ExceptionHandler({
            QuoteNotFoundException.class,
            UserNotFoundException.class
    })
    public ResponseEntity<String> handleNotFoundException(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler({
            QuoteUpdateDeniedException.class,
            QuoteDeletionDeniedException.class
    })
    public ResponseEntity<String> handleAccessDeniedException(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(exception.getMessage());
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<String> handleConstraintException(ConstraintViolationException exception) {
        return ResponseEntity
                .badRequest()
                .body("Quote creation error. Check the quote data is correct");
    }
}
