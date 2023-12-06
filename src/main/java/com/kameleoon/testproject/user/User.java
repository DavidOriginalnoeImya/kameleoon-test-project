package com.kameleoon.testproject.user;

import com.kameleoon.testproject.user.dto.AddUserDTO;
import com.kameleoon.testproject.user.dto.UserDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter @Setter
public class User {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private Long id;

    private String name;

    private String email;

    private String password;

    @Setter(AccessLevel.NONE)
    private LocalDate creationDate = LocalDate.now();

    public static User create(AddUserDTO addUserDTO) {
        User user = new User();
        user.setName(addUserDTO.getName());
        user.setEmail(addUserDTO.getEmail());
        user.setPassword(addUserDTO.getPassword());
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
}
