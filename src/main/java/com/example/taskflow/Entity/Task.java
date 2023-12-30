package com.example.taskflow.Entity;

import com.example.taskflow.Entity.Enums.StatusTask;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime expDate;
    @Enumerated(EnumType.STRING)
    private StatusTask statusTask;
    private LocalDateTime assignedDate;
    private Long userAssignedBefore;
    private boolean hasChanged;
    private LocalDateTime dateCreate;
    @ManyToOne
    private User createBy ;
    @ManyToMany
    @JoinTable(
            name = "task_tags",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    @ManyToOne
    @JsonBackReference
    private User user;

    @OneToOne
    private TaskChangeRequest taskChangeRequest;

}
