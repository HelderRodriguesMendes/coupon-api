package com.testepratico.couponapi.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageException {

    CODE_INVALID("O código do cupom deve conter exatamente 6 caracteres alfanuméricos"),
    CODE_REQUIRED("O código do cupom é obrigatório"),

    DESCRIPTION_REQUIRED("A descrição do cupom é obrigatória"),

    DISCOUNT_VALUE_INVALID("O valor do desconto deve ser no mínimo 0.5"),
    DISCOUNT_VALUE_REQUIRED("O valor do desconto é obrigatório"),

    EXPIRATION_DATE_INVALID("A data de expiração do cupom não pode estar no passado."),
    EXPIRATION_DATE_REQUIRED("A data de expiração do cupom é obrigatória"),

    COUPON_STATUS_DELETED("O cupom informado já foi excluído"),
    COUPON_NOT_FOUND("Cupom não encontrado");

    private final String message;
}