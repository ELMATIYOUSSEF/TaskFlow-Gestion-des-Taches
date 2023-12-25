package com.example.taskflow.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String username;
    private String password;
    private Boolean SupToken;
    private int rmpToken;
    private LocalDateTime dateForDouble;
    @ManyToMany
    private List<Role> authorities;
    @OneToMany(mappedBy = "user")
    List<Task> tasks ;


}
