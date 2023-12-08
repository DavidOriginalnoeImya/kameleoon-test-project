package com.kameleoon.testproject.user;

import com.kameleoon.testproject.user.dto.AddUserDTO;
import com.kameleoon.testproject.user.dto.UserDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Base64;

@Entity
@Table(name = "users")
@Getter @Setter
public class User {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull @NotEmpty
    private String name;

    @Email
    @NotNull @NotEmpty
    @Column(unique = true)
    private String email;

    @NotNull @NotEmpty
    private String password;

    @Setter(AccessLevel.NONE)
    private ZonedDateTime creationDate = ZonedDateTime.now();

    public static User create(AddUserDTO addUserDTO) {
        User user = new User();
        user.setName(addUserDTO.getName());
        user.setEmail(addUserDTO.getEmail());
        user.setPassword(encryptPassword(addUserDTO.getPassword()));
        return user;
    }

    public UserDTO toDTO() {
        return UserDTO.builder()
                .id(getId())
                .name(getName())
                .email(getEmail())
                .creationDate(getCreationDate())
                .build();
    }

    private static String encryptPassword(String password) {
        return Base64.getEncoder().encodeToString(password.getBytes());
    }
}
