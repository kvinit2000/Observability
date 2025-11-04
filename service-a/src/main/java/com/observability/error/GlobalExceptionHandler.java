package com.observability.error;

import com.observability.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handle(RuntimeException ex,
                                                HttpServletRequest req) {
        // we expose our own ErrorResponse now
        ErrorResponse body = new ErrorResponse(ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(500).body(body);
    }
}
