package com.sigma.dao.error.handler;

import com.sigma.dao.error.ErrorCode;
import com.sigma.dao.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.Objects;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> error(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse().setError(ex.getMessage()).setCode("E9999"));
    }
    @Override
    protected @NotNull ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NotNull HttpHeaders headers,
            @NotNull HttpStatus status,
            @NotNull WebRequest request) {
        String error = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
        final String[] code = {"E9999"};
        Arrays.stream(ErrorCode.class.getDeclaredFields()).forEach(field -> {
            try {
                String message = field.get(null).toString();
                if(Objects.requireNonNull(error).equals(message)) {
                    code[0] = field.getName();
                }
            } catch(Exception e) {
                // do nothing
            }
        });
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse().setError(error).setCode(code[0]));
    }
}