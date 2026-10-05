package com.testepratico.couponapi.application.usecase;

import com.testepratico.couponapi.application.port.CouponRepositoryPort;
import com.testepratico.couponapi.domain.enums.CouponStatus;
import com.testepratico.couponapi.domain.exception.CouponDomainException;
import com.testepratico.couponapi.domain.exception.CouponNotFoundException;
import com.testepratico.couponapi.domain.model.CouponModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteCouponUseCaseTest {

    @Mock
    private CouponRepositoryPort couponRepositoryPort;

    private DeleteCouponUseCase deleteCouponUseCase;

    @BeforeEach
    void setUp() {
        deleteCouponUseCase =
            new DeleteCouponUseCase(couponRepositoryPort);
    }

    @Test
    void shouldDeleteCouponSuccessfully() {

        UUID id = UUID.randomUUID();

        CouponModel couponModel = CouponModel.restore(
            id,
            "ABC123",
            "Cupom de teste",
            new BigDecimal("0.8"),
            Instant.parse("2099-12-31T23:59:59Z"),
            CouponStatus.ACTIVE,
            false,
            false
        );

        when(couponRepositoryPort.findById(id))
            .thenReturn(Optional.of(couponModel));

        when(couponRepositoryPort.save(any(CouponModel.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        deleteCouponUseCase.deleteCoupon(id);

        assertThat(couponModel.getStatus())
            .isEqualTo(CouponStatus.DELETED);

        verify(couponRepositoryPort, times(1))
            .findById(id);

        verify(couponRepositoryPort, times(1))
            .save(couponModel);
    }

    @Test
    void shouldPreserveCouponDataWhenDeleting() {

        UUID id = UUID.randomUUID();

        CouponModel couponModel = CouponModel.restore(
            id,
            "ABC123",
            "Cupom de teste",
            new BigDecimal("0.8"),
            Instant.parse("2099-12-31T23:59:59Z"),
            CouponStatus.ACTIVE,
            true,
            false
        );

        when(couponRepositoryPort.findById(id))
            .thenReturn(Optional.of(couponModel));

        when(couponRepositoryPort.save(any(CouponModel.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        deleteCouponUseCase.deleteCoupon(id);

        assertThat(couponModel.getId()).isEqualTo(id);
        assertThat(couponModel.getCode()).isEqualTo("ABC123");
        assertThat(couponModel.getDescription())
            .isEqualTo("Cupom de teste");
        assertThat(couponModel.getDiscountValue())
            .isEqualByComparingTo("0.8");
        assertThat(couponModel.isPublished()).isTrue();
        assertThat(couponModel.isRedeemed()).isFalse();

        assertThat(couponModel.getStatus())
            .isEqualTo(CouponStatus.DELETED);

        verify(couponRepositoryPort).save(couponModel);
    }

    @Test
    void shouldThrowExceptionWhenCouponDoesNotExist() {

        UUID id = UUID.randomUUID();

        when(couponRepositoryPort.findById(id))
            .thenReturn(Optional.empty());

        assertThatThrownBy(
            () -> deleteCouponUseCase.deleteCoupon(id)
        )
            .isInstanceOf(CouponNotFoundException.class)
            .hasMessage("Cupom não encontrado");

        verify(couponRepositoryPort, times(1))
            .findById(id);

        verify(couponRepositoryPort, never())
            .save(any(CouponModel.class));
    }

    @Test
    void shouldNotDeleteCouponTwice() {

        UUID id = UUID.randomUUID();

        CouponModel couponModel = CouponModel.restore(
            id,
            "ABC123",
            "Cupom de teste",
            new BigDecimal("0.8"),
            Instant.parse("2099-12-31T23:59:59Z"),
            CouponStatus.DELETED,
            false,
            false
        );

        when(couponRepositoryPort.findById(id))
            .thenReturn(Optional.of(couponModel));

        assertThatThrownBy(
            () -> deleteCouponUseCase.deleteCoupon(id)
        )
            .isInstanceOf(CouponDomainException.class);

        assertThat(couponModel.getStatus())
            .isEqualTo(CouponStatus.DELETED);

        verify(couponRepositoryPort, times(1))
            .findById(id);

        verify(couponRepositoryPort, never())
            .save(any(CouponModel.class));
    }
}