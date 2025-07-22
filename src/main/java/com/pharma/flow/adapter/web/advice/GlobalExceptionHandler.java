package com.pharma.flow.adapter.web.advice;

import com.pharma.flow.adapter.web.exception.ApplicationException;
import com.pharma.flow.adapter.web.exception.NotFoundException;
import com.pharma.flow.adapter.web.exception.UnprocessableException;
import com.pharma.flow.adapter.web.exception.ValidationException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public Mono<Void> handleNotFound(NotFoundException ex, ServerWebExchange exchange) {
        return writeError(exchange, HttpStatus.NOT_FOUND, ex.getErrorCode(), ex.getMessage());
    }

    @ExceptionHandler(UnprocessableException.class)
    public Mono<Void> handleUnprocessable(UnprocessableException ex, ServerWebExchange exchange) {
        return writeError(exchange, HttpStatus.UNPROCESSABLE_ENTITY, ex.getErrorCode(), ex.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public Mono<Void> handleValidation(ValidationException ex, ServerWebExchange exchange) {
        return writeError(exchange, HttpStatus.BAD_REQUEST, ex.getErrorCode(), ex.getMessage());
    }

    @ExceptionHandler(ApplicationException.class)
    public Mono<Void> handleBusiness(ApplicationException ex, ServerWebExchange exchange) {
        return writeError(exchange, HttpStatus.BAD_REQUEST, ex.getErrorCode(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Mono<Void> handleOther(Exception ex, ServerWebExchange exchange) {
        return writeError(exchange, HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", ex.getMessage());
    }

    private Mono<Void> writeError(ServerWebExchange exchange, HttpStatus status, String code, String message) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        var errorBody = Map.of(
                "code", code,
                "message", message);
        var buffer =
                exchange.getResponse().bufferFactory().wrap(errorBody.toString().getBytes());

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
