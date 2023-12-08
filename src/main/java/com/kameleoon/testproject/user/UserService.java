package com.kameleoon.testproject.user;

import com.kameleoon.testproject.user.dto.AddUserDTO;
import com.kameleoon.testproject.user.dto.UserDTO;
import com.kameleoon.testproject.user.exceptions.UserAlreadyExistsException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO addUser(AddUserDTO addUserDTO) {
        if (!userRepository.existsByEmail(addUserDTO.getEmail())) {
            User user = userRepository.save(User.create(addUserDTO));
            return user.toDTO();
        }

        throw new UserAlreadyExistsException(
                String.format("User with email %s already exists", addUserDTO.getEmail())
        );
    }
}
