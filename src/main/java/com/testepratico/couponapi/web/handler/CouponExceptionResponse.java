package com.testepratico.couponapi.web.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class CouponExceptionResponse {

    private final HttpStatus status;
    private final String message;
    private final Object errors;
}