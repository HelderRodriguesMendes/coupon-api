package com.testepratico.couponapi.domain.model;

import com.testepratico.couponapi.domain.enums.CouponStatus;
import com.testepratico.couponapi.domain.enums.MessageException;
import com.testepratico.couponapi.domain.exception.CouponDomainException;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class CouponModel {

    private UUID id;
    private String code;
    private String description;
    private BigDecimal discountValue;
    private Instant expirationDate;
    private CouponStatus status;
    private boolean published;
    private boolean redeemed;

    public static CouponModel create(
        String code, String description, BigDecimal discountValue,
        Instant expirationDate, boolean published){

        validDiscountValue(discountValue);
        validExpirationDate(expirationDate);
        return CouponModel.builder()
                .id(UUID.randomUUID())
                .code(validCode(code))
                .description(description)
                .discountValue(discountValue)
                .expirationDate(expirationDate)
                .status(CouponStatus.ACTIVE)
                .published(published)
                .redeemed(false)
                .build();
    }

    public static CouponModel restore(
        UUID id, String code, String description,
        BigDecimal discountValue, Instant expirationDate,
        CouponStatus status, boolean published, boolean redeemed){

        return CouponModel.builder()
                .id(id)
                .code(code)
                .description(description)
                .discountValue(discountValue)
                .expirationDate(expirationDate)
                .status(status)
                .published(published)
                .redeemed(redeemed)
                .build();
    }

    public void delete() {
        validateCouponStatus();
        this.status = CouponStatus.DELETED;
    }

    private static String validCode(String code) {
        code = code.replaceAll("[^a-zA-Z0-9]", "");
        if(code.length() != 6){
            throw new CouponDomainException(MessageException.CODE_INVALID.getMessage());
        }
        return code;
    }

    private static void validDiscountValue(BigDecimal discountValue) {
        BigDecimal minimumDiscountValue = new BigDecimal("0.5");
        if(minimumDiscountValue.compareTo(discountValue) > 0){
            throw new CouponDomainException(MessageException.DISCOUNT_VALUE_INVALID.getMessage());
        }
    }

    private static void validExpirationDate(Instant expirationDate) {
        Instant now = Instant.now();
        if(expirationDate.isBefore(now)){
            throw new CouponDomainException(MessageException.EXPIRATION_DATE_INVALID.getMessage());
        }
    }

    private void validateCouponStatus(){
        if (this.status == CouponStatus.DELETED) {
            throw new CouponDomainException(MessageException.COUPON_STATUS_DELETED.getMessage());
        }
    }
}