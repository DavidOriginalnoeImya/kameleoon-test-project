package com.kameleoon.testproject.quote;

import com.kameleoon.testproject.quote.dto.AddQuoteDTO;
import com.kameleoon.testproject.quote.dto.QuoteDTO;
import com.kameleoon.testproject.quote.dto.UpdateQuoteDTO;
import com.kameleoon.testproject.quote.exceptions.QuoteDeletionDeniedException;
import com.kameleoon.testproject.quote.exceptions.QuoteNotFoundException;
import com.kameleoon.testproject.quote.exceptions.QuoteUpdateDeniedException;
import com.kameleoon.testproject.user.exceptions.UserNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    /**
     * Метод для получения списка всех доступных цитат
     */
    @GetMapping
    public ResponseEntity<List<QuoteDTO>> getQuotes() {
        return ResponseEntity
                .ok(quoteService.getQuotes());
    }

    /**
     * Метод для получения списка цитат в порядке убывания их рейтинга
     * @param limit - ограничение на количество возвращаемых цитат
     */
    @GetMapping("/top")
    public ResponseEntity<List<QuoteDTO>> getQuoteTop(@RequestParam Integer limit) {
        return ResponseEntity
                .ok(quoteService.getQuotes(limit, QuoteSortDirection.TOP));
    }

    /**
     * Метод для получения списка цитат в порядке возрастания их рейтинга
     * @param limit - ограничение на количество возвращаемых цитат
     */
    @GetMapping("/flop")
    public ResponseEntity<List<QuoteDTO>> getQuoteFlop(@RequestParam Integer limit) {
        return ResponseEntity
                .ok(quoteService.getQuotes(limit, QuoteSortDirection.FLOP));
    }

    /**
     * Метод для получения одной конкретной цитаты
     * @param id - идентификатор цитаты
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuoteDTO> getQuote(@PathVariable Long id) {
        return ResponseEntity
                .ok(quoteService.getQuote(id));
    }

    /**
     * Метод для получения одной рандомной цитаты
     */
    @GetMapping("/random")
    public ResponseEntity<QuoteDTO> getRandomQuote() {
        return ResponseEntity
                .ok(quoteService.getRandomQuote());
    }

    /**
     * Метод для добавления новой цитаты
     * @param addQuoteDTO - dto, содержащий данные добавляемой цитаты
     * @param userEmail - http-заголовок, содержащий email пользователя, добавляющего цитату
     */
    @PostMapping
    public ResponseEntity<QuoteDTO> addQuote(
        @RequestBody @Valid AddQuoteDTO addQuoteDTO,
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

    /**
     * Метод для добавления положительного рейтинга цитаты
     * @param id - идентификатор оцениваемой цитаты
     * @param userEmail - http-заголовок, содержащий email пользователя, оценивающего цитату
     */
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

    /**
     * Метод для добавления отрицательного рейтинга цитаты
     * @param id - идентификатор оцениваемой цитаты
     * @param userEmail - http-заголовок, содержащий email пользователя, оценивающего цитату
     */
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

    /**
     * Метод для обновления существующей цитаты
     * @param updateQuoteDTO - dto, содержащий данные обновляемой цитаты
     * @param userEmail - http-заголовок, содержащий email пользователя, обновляющего цитату
     */
    @PutMapping
    public ResponseEntity<QuoteDTO> updateQuote(
        @RequestBody @Valid UpdateQuoteDTO updateQuoteDTO,
        @RequestHeader(name = "Email", defaultValue = "test@mail.com") String userEmail
    ) {
        updateQuoteDTO.setUserEmail(userEmail);
        return ResponseEntity
                .ok(quoteService.updateQuote(updateQuoteDTO));
    }


    /**
     * Метод для удаления существующей цитаты
     * @param id - идентификатор удаляемой цитаты
     * @param userEmail - http-заголовок, содержащий email пользователя, удаляющего цитату
     */
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

    @ExceptionHandler({ QuoteNotFoundException.class, UserNotFoundException.class })
    public ResponseEntity<String> handleNotFoundException(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler({ QuoteUpdateDeniedException.class, QuoteDeletionDeniedException.class})
    public ResponseEntity<String> handleAccessDeniedException(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(exception.getMessage());
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<String> handleConstraintException() {
        return ResponseEntity
                .badRequest()
                .body("Quote creation error. Check the quote data is correct");
    }
}
