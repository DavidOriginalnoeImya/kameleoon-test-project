package com.kameleoon.testproject.user;

import com.kameleoon.testproject.user.dto.AddUserDTO;
import com.kameleoon.testproject.user.dto.UserDTO;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> addUser(@RequestBody AddUserDTO addUserDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.addUser(addUserDTO));
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<String> handleConstraintException(ConstraintViolationException exception) {
        return ResponseEntity
                .badRequest()
                .body("User creation error. Check the user data is correct");
    }
}
