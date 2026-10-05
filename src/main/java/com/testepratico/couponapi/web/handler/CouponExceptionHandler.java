package com.testepratico.couponapi.web.handler;

import com.testepratico.couponapi.domain.exception.CouponDomainException;
import com.testepratico.couponapi.domain.exception.CouponNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class CouponExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String ERROR_INVALID_DATA =
        "Erro de validação dos campos";

    @ExceptionHandler(CouponDomainException.class)
    protected ResponseEntity<CouponExceptionResponse> handleCouponDomainException(
        CouponDomainException ex) {

        log.warn("Regra de negócio violada: {}", ex.getMessage());

        CouponExceptionResponse response =
            new CouponExceptionResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                null
            );

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
            .getFieldErrors()
            .forEach(error ->
                errors.put(
                    error.getField(),
                    error.getDefaultMessage()
                )
            );

        log.warn("Erro de validação na requisição: {}", errors);

        CouponExceptionResponse response =
            new CouponExceptionResponse(
                HttpStatus.BAD_REQUEST,
                ERROR_INVALID_DATA,
                errors
            );

        return handleExceptionInternal(
            ex,
            response,
            headers,
            HttpStatus.BAD_REQUEST,
            request
        );
    }

    @ExceptionHandler(CouponNotFoundException.class)
    protected ResponseEntity<CouponExceptionResponse> handleCouponNotFoundException(
        CouponNotFoundException ex) {

        log.warn("Cupom não encontrado: {}", ex.getMessage());

        CouponExceptionResponse response =
            new CouponExceptionResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                null
            );

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(response);
    }
}