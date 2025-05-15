package com.sh32bit.response;

import java.time.LocalDateTime;

public record ExceptionResponse(
        String message,
        String error,
        LocalDateTime timestamp
) {
}
