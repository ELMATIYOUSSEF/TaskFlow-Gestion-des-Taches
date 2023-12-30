package com.example.taskflow.Dto.response;

import com.example.taskflow.Dto.TaskDTO;
import com.example.taskflow.Entity.Role;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVM {
        private Long id;
        private String email;
        private String username;
        private Boolean SupToken;
        private int rmpToken;
        private LocalDateTime dateForDouble;
        private List<Role> authorities;
       // private List<TaskDTO> tasks ;
}
