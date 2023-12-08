package com.kameleoon.testproject.user;

import com.kameleoon.testproject.user.dto.AddUserDTO;
import com.kameleoon.testproject.user.dto.UserDTO;
import com.kameleoon.testproject.user.exceptions.UserAlreadyExistsException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Метод для добавления нового пользователя
     * @param addUserDTO - dto, содержащий данные добавляемого пользователя
     */
    @PostMapping
    public ResponseEntity<UserDTO> addUser(@RequestBody @Valid AddUserDTO addUserDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.addUser(addUserDTO));
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<String> handleConstraintException() {
        return ResponseEntity
                .badRequest()
                .body("User creation error. Check the user data is correct");
    }

    @ExceptionHandler({ UserAlreadyExistsException.class })
    public ResponseEntity<String> handleUserAlreadyExistsException(
            UserAlreadyExistsException exception
    ) {
        return ResponseEntity
                .badRequest()
                .body(exception.getMessage());
    }
}
