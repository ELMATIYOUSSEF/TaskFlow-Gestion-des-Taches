package com.example.taskflow.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagDTO {
    private Long tagId;
    @NotBlank
    private String tagName;
}
