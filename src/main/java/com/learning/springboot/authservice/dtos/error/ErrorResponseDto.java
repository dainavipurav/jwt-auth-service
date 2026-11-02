package com.learning.springboot.authservice.dtos.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponseDto {

    private int status;
    private String message;
    private String path;
    private LocalDateTime timestamp;

}
