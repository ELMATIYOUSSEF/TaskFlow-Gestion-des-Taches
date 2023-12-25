package com.example.taskflow.Entity;

import com.example.taskflow.Entity.Enums.StatusRequest;
import com.example.taskflow.Entity.Enums.TokenType;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskChangeRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dateRequest;

    @Enumerated(EnumType.STRING)
    private StatusRequest status;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @OneToOne
    private Task task;

}
