package com.example.taskflow.Dto;

import com.example.taskflow.Dto.response.UserVM;
import com.example.taskflow.Entity.Enums.StatusTask;
import com.example.taskflow.Entity.TaskChangeRequest;
import com.example.taskflow.Entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDTO {
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private LocalDateTime expDate;
    @Enumerated(EnumType.STRING)
    private StatusTask statusTask;
    @NonNull
    private List<TagDTO> tags;
    @NotBlank
    private Long userAssignedBefore;
    private LocalDateTime assignedDate;
    private boolean hasChanged;
    private UserVM user;
    private TaskChangeRequest taskChangeRequest;

/*
    public void setAssignedDate(LocalDateTime dateAssigned) {
        if (dateAssigned != null && dateAssigned.isBefore(LocalDateTime.now().plusDays(3))) {
            throw new IllegalArgumentException("Date Assigned should be after 3 day from Now");
        }
        this.assignedDate = dateAssigned;
    }
    public void setExpDate(LocalDateTime dueDate) {
        if (dueDate != null && dueDate.isBefore(this.expDate)) {
            throw new IllegalArgumentException("Due date cannot be Before Date Assigned");
        }
        this.expDate = dueDate;
    }*/
   /* public void setTags(List<TagDTO> tags) {
        if (tags == null || checkNumberTask(tags)){
            throw new IllegalArgumentException("At least one tag must be provided");
        }
        this.tags = tags;
    }*/

    public boolean checkNumberTask(List<TagDTO> tags){
        return tags.size() <= 1;
    }
}
