package com.example.taskflow.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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
    private String username;
    private String password;
    private Boolean SupToken;
    private int rmpToken;
    private LocalDate dateForDouble;
    @ManyToMany
    private List<Role> authorities;
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    List<Task> tasks ;


}
