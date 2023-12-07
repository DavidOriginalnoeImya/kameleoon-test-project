package com.kameleoon.testproject.quote;

import com.kameleoon.testproject.quote.dto.AddQuoteDTO;
import com.kameleoon.testproject.quote.dto.QuoteDTO;
import com.kameleoon.testproject.quote.dto.UpdateQuoteDTO;
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
    public ResponseEntity<List<QuoteDTO>> getQuotes(
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String rate
    ) {
        if (limit != null && rate != null) {
            return ResponseEntity
                    .ok(quoteService.getQuotes(limit, rate));
        }

        return ResponseEntity
                .ok(quoteService.getQuotes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuoteDTO> getQuote(@PathVariable Long id) {
        return ResponseEntity
                .ok(quoteService.getQuote(id));
    }

    @PostMapping
    public ResponseEntity<QuoteDTO> addQuote(@RequestBody AddQuoteDTO addQuoteDTO) {
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
    public ResponseEntity<QuoteDTO> updateQuote(@RequestBody UpdateQuoteDTO updateQuoteDTO) {
        return ResponseEntity
                .ok(quoteService.updateQuote(updateQuoteDTO));
    }

    @GetMapping("/random")
    public ResponseEntity<QuoteDTO> getRandomQuote() {
        return ResponseEntity
                .ok(quoteService.getRandomQuote());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuote(@PathVariable Long id) {
        quoteService.deleteQuote(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
