package com.szczuka.marcin.test.dto;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostDto {

    private Long userId;
    @Size(max = 140, message = "Message can contain max 140 signs.")
    private String content;
}
