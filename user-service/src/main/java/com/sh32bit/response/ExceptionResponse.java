package com.sh32bit.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {
    private String message;
    private String error;
    private LocalDateTime timestamp;
}
