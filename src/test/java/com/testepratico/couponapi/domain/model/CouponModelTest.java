package com.testepratico.couponapi.domain.model;

import com.testepratico.couponapi.domain.enums.CouponStatus;
import com.testepratico.couponapi.domain.enums.MessageException;
import com.testepratico.couponapi.domain.exception.CouponDomainException;
import com.testepratico.couponapi.domain.exception.CouponNotFoundException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class CouponModelTest {

    @Test
    void shouldCreateCouponSuccessfully() {
        String code = "ABC123";
        String description = "Cupom de teste";
        BigDecimal discountValue = new BigDecimal("10.00");
        Instant expirationDate = Instant.now().plusSeconds(3600);

        CouponModel coupon = CouponModel.create(
            code,
            description,
            discountValue,
            expirationDate,
            false
        );

        assertAll(
            () -> assertNotNull(coupon),
            () -> assertNotNull(coupon.getId()),
            () -> assertEquals(code, coupon.getCode()),
            () -> assertEquals(description, coupon.getDescription()),
            () -> assertEquals(discountValue, coupon.getDiscountValue()),
            () -> assertEquals(expirationDate, coupon.getExpirationDate()),
            () -> assertEquals(CouponStatus.ACTIVE, coupon.getStatus()),
            () -> assertFalse(coupon.isPublished()),
            () -> assertFalse(coupon.isRedeemed())
        );
    }

    @Test
    void shouldCreatePublishedCoupon() {
        CouponModel coupon = CouponModel.create(
            "ABC123",
            "Cupom publicado",
            new BigDecimal("10.00"),
            Instant.now().plusSeconds(3600),
            true
        );

        assertTrue(coupon.isPublished());
        assertEquals(CouponStatus.ACTIVE, coupon.getStatus());
        assertFalse(coupon.isRedeemed());
    }

    @Test
    void shouldThrowExceptionWhenCodeIsNull() {
        assertThatThrownBy(() ->
            CouponModel.create(
                null,
                "Cupom de teste",
                new BigDecimal("10.00"),
                Instant.now().plusSeconds(3600),
                false
            )
        )
            .isInstanceOf(CouponDomainException.class)
            .hasMessage(MessageException.CODE_REQUIRED.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCodeIsBlank() {
        assertThatThrownBy(() ->
            CouponModel.create(
                "   ",
                "Cupom de teste",
                new BigDecimal("10.00"),
                Instant.now().plusSeconds(3600),
                false
            )
        )
            .isInstanceOf(CouponDomainException.class)
            .hasMessage(MessageException.CODE_REQUIRED.getMessage());
    }

    @Test
    void shouldRemoveSpecialCharactersFromCode() {
        CouponModel coupon = CouponModel.create(
            "ABC-123",
            "Cupom de teste",
            new BigDecimal("10.00"),
            Instant.now().plusSeconds(3600),
            false
        );

        assertEquals("ABC123", coupon.getCode());
    }

    @Test
    void shouldRemoveMultipleSpecialCharactersFromCode() {
        CouponModel coupon = CouponModel.create(
            "A@B#C-1_2!3",
            "Cupom de teste",
            new BigDecimal("10.00"),
            Instant.now().plusSeconds(3600),
            false
        );

        assertEquals("ABC123", coupon.getCode());
    }

    @Test
    void shouldThrowExceptionWhenSanitizedCodeHasLessThanSixCharacters() {
        assertThatThrownBy(() ->
            CouponModel.create(
                "ABC-12",
                "Cupom de teste",
                new BigDecimal("10.00"),
                Instant.now().plusSeconds(3600),
                false
            )
        )
            .isInstanceOf(CouponDomainException.class)
            .hasMessage(MessageException.CODE_INVALID.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSanitizedCodeHasMoreThanSixCharacters() {
        assertThatThrownBy(() ->
            CouponModel.create(
                "ABC-1234",
                "Cupom de teste",
                new BigDecimal("10.00"),
                Instant.now().plusSeconds(3600),
                false
            )
        )
            .isInstanceOf(CouponDomainException.class)
            .hasMessage(MessageException.CODE_INVALID.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsNull() {
        assertThatThrownBy(() ->
            CouponModel.create(
                "ABC123",
                null,
                new BigDecimal("10.00"),
                Instant.now().plusSeconds(3600),
                false
            )
        )
            .isInstanceOf(CouponDomainException.class)
            .hasMessage(MessageException.DESCRIPTION_REQUIRED.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsBlank() {
        assertThatThrownBy(() ->
            CouponModel.create(
                "ABC123",
                "   ",
                new BigDecimal("10.00"),
                Instant.now().plusSeconds(3600),
                false
            )
        )
            .isInstanceOf(CouponDomainException.class)
            .hasMessage(MessageException.DESCRIPTION_REQUIRED.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDiscountValueIsNull() {
        assertThatThrownBy(() ->
            CouponModel.create(
                "ABC123",
                "Cupom de teste",
                null,
                Instant.now().plusSeconds(3600),
                false
            )
        )
            .isInstanceOf(CouponDomainException.class)
            .hasMessage(MessageException.DISCOUNT_VALUE_REQUIRED.getMessage());
    }

    @Test
    void shouldCreateCouponWhenDiscountValueIsMinimumAllowed() {
        BigDecimal minimumDiscountValue = new BigDecimal("0.5");

        CouponModel coupon = CouponModel.create(
            "ABC123",
            "Cupom de teste",
            minimumDiscountValue,
            Instant.now().plusSeconds(3600),
            false
        );

        assertEquals(
            0,
            minimumDiscountValue.compareTo(coupon.getDiscountValue())
        );
    }

    @Test
    void shouldThrowExceptionWhenDiscountValueIsLessThanMinimum() {
        assertThatThrownBy(() ->
            CouponModel.create(
                "ABC123",
                "Cupom de teste",
                new BigDecimal("0.49"),
                Instant.now().plusSeconds(3600),
                false
            )
        )
            .isInstanceOf(CouponDomainException.class)
            .hasMessage(MessageException.DISCOUNT_VALUE_INVALID.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDiscountValueIsZero() {
        assertThatThrownBy(() ->
            CouponModel.create(
                "ABC123",
                "Cupom de teste",
                BigDecimal.ZERO,
                Instant.now().plusSeconds(3600),
                false
            )
        )
            .isInstanceOf(CouponDomainException.class)
            .hasMessage(MessageException.DISCOUNT_VALUE_INVALID.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDiscountValueIsNegative() {
        assertThatThrownBy(() ->
            CouponModel.create(
                "ABC123",
                "Cupom de teste",
                new BigDecimal("-1"),
                Instant.now().plusSeconds(3600),
                false
            )
        )
            .isInstanceOf(CouponDomainException.class)
            .hasMessage(MessageException.DISCOUNT_VALUE_INVALID.getMessage());
    }

    @Test
    void shouldCreateCouponWithLargeDiscountValue() {
        BigDecimal discountValue = new BigDecimal("999999.99");

        CouponModel coupon = CouponModel.create(
            "ABC123",
            "Cupom de teste",
            discountValue,
            Instant.now().plusSeconds(3600),
            false
        );

        assertEquals(
            0,
            discountValue.compareTo(coupon.getDiscountValue())
        );
    }

    @Test
    void shouldThrowExceptionWhenExpirationDateIsNull() {
        assertThatThrownBy(() ->
            CouponModel.create(
                "ABC123",
                "Cupom de teste",
                new BigDecimal("10.00"),
                null,
                false
            )
        )
            .isInstanceOf(CouponDomainException.class)
            .hasMessage(MessageException.EXPIRATION_DATE_REQUIRED.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenExpirationDateIsInThePast() {
        Instant expirationDate = Instant.now().minusSeconds(3600);

        assertThatThrownBy(() ->
            CouponModel.create(
                "ABC123",
                "Cupom de teste",
                new BigDecimal("10.00"),
                expirationDate,
                false
            )
        )
            .isInstanceOf(CouponDomainException.class)
            .hasMessage(MessageException.EXPIRATION_DATE_INVALID.getMessage());
    }

    @Test
    void shouldCreateCouponWhenExpirationDateIsInTheFuture() {
        Instant expirationDate = Instant.now().plusSeconds(3600);

        CouponModel coupon = CouponModel.create(
            "ABC123",
            "Cupom de teste",
            new BigDecimal("10.00"),
            expirationDate,
            false
        );

        assertEquals(expirationDate, coupon.getExpirationDate());
    }

    @Test
    void shouldDeleteCouponSuccessfully() {
        CouponModel coupon = CouponModel.create(
            "ABC123",
            "Cupom de teste",
            new BigDecimal("10.00"),
            Instant.now().plusSeconds(3600),
            false
        );

        coupon.delete();

        assertEquals(CouponStatus.DELETED, coupon.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenDeletingAlreadyDeletedCoupon() {
        CouponModel coupon = CouponModel.restore(
            UUID.randomUUID(),
            "ABC123",
            "Cupom de teste",
            new BigDecimal("0.8"),
            Instant.parse("2099-12-31T23:59:59Z"),
            CouponStatus.DELETED,
            false,
            false
        );

        assertThatThrownBy(coupon::delete)
            .isInstanceOf(CouponDomainException.class)
            .hasMessage(MessageException.COUPON_STATUS_DELETED.getMessage());

        assertThat(coupon.getStatus())
            .isEqualTo(CouponStatus.DELETED);
    }

    @Test
    void shouldReturnTrueWhenCouponIsDeleted() {
        CouponModel coupon = CouponModel.restore(
            UUID.randomUUID(),
            "ABC123",
            "Cupom de teste",
            new BigDecimal("0.8"),
            Instant.parse("2099-12-31T23:59:59Z"),
            CouponStatus.DELETED,
            false,
            false
        );

        assertThat(coupon.isDeleted()).isTrue();
    }

    @Test
    void shouldReturnFalseWhenCouponIsNotDeleted() {
        CouponModel coupon = CouponModel.restore(
            UUID.randomUUID(),
            "ABC123",
            "Cupom de teste",
            new BigDecimal("0.8"),
            Instant.parse("2099-12-31T23:59:59Z"),
            CouponStatus.ACTIVE,
            false,
            false
        );

        assertThat(coupon.isDeleted()).isFalse();
    }

    @Test
    void shouldThrowExceptionWhenValidatingDeletedCouponAvailability() {
        CouponModel coupon = CouponModel.restore(
            UUID.randomUUID(),
            "ABC123",
            "Cupom de teste",
            new BigDecimal("0.8"),
            Instant.parse("2099-12-31T23:59:59Z"),
            CouponStatus.DELETED,
            false,
            false
        );

        assertThatThrownBy(coupon::validateAvailable)
            .isInstanceOf(CouponNotFoundException.class)
            .hasMessage(MessageException.COUPON_NOT_FOUND.getMessage());
    }

    @Test
    void shouldValidateAvailableCouponSuccessfully() {
        CouponModel coupon = CouponModel.restore(
            UUID.randomUUID(),
            "ABC123",
            "Cupom de teste",
            new BigDecimal("0.8"),
            Instant.parse("2099-12-31T23:59:59Z"),
            CouponStatus.ACTIVE,
            false,
            false
        );

        assertThatCode(coupon::validateAvailable)
            .doesNotThrowAnyException();
    }
}