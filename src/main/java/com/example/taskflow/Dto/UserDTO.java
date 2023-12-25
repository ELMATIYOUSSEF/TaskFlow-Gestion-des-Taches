package com.example.taskflow.Dto;

import com.example.taskflow.Entity.Role;
import com.example.taskflow.Entity.Task;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    @Email
    private String email;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private Boolean SupToken;
    @NotBlank
    private int rmpToken;
    private LocalDateTime dateForDouble;
    private List<Role> authorities;
    private List<Task> tasks ;
    public void setPassword(String pass) {
        this.password = BCrypt.hashpw(pass, BCrypt.gensalt());
    }
}
