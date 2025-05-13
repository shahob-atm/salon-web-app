package com.sh32bit.exception;

import com.sh32bit.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<ExceptionResponse> buildErrorResponse(Exception ex, WebRequest req, HttpStatus status) {
        ExceptionResponse response = new ExceptionResponse(
                ex.getMessage(),
                req.getDescription(false),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ExceptionResponse> handleUserException(UserException ex, WebRequest req) {
        return buildErrorResponse(ex, req, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ReviewException.class)
    public ResponseEntity<ExceptionResponse> handleReviewException(ReviewException ex, WebRequest req) {
        return buildErrorResponse(ex, req, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGeneralException(Exception ex, WebRequest req) {
        return buildErrorResponse(ex, req, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
