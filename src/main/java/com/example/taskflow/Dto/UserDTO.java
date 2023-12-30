package com.example.taskflow.Dto;

import com.example.taskflow.Entity.Role;
import com.example.taskflow.Entity.Task;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

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
    private Boolean SupToken;
    private int rmpToken;
    private LocalDateTime dateForDouble;
    private List<Role> authorities;
   // private List<TaskDTO> tasks ;

   /* public void setPassword(String pass) {
        this.password = BCrypt.hashpw(pass, BCrypt.gensalt());
    } */

    public void setSupToken(Boolean bool ) {
        if(bool == null) bool = false ;
        this.SupToken = bool;
    }
    public void setRmpToken(int token ) {
        if(token ==0) token = 2 ;
        this.rmpToken = token;
    }
}
