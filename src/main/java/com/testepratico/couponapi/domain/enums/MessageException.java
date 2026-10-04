package com.testepratico.couponapi.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageException {
    CODE_INVALID("O código do cupom deve conter exatamente 6 caracteres alfanuméricos"),
    DISCOUNT_VALUE_INVALID("O valor do desconto deve ser no mínimo 0.5"),
    COUPON_STATUS_DELETED("O cupom informado já foi excluído"),
    EXPIRATION_DATE_INVALID("A data de expiração do cupom não pode estar no passado.");

    private final String message;
}