package com.learning.springboot.authservice.dtos.success;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuccessResponseDto {
    private int status;
    private String message;
    private LocalDateTime timestamp;
}
