package com.testepratico.couponapi.application.usecase;

import com.testepratico.couponapi.application.port.CouponRepositoryPort;
import com.testepratico.couponapi.domain.enums.CouponStatus;
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
class GetCouponUseCaseTest {

    @Mock
    private CouponRepositoryPort couponRepositoryPort;

    private GetCouponUseCase getCouponUseCase;

    @BeforeEach
    void setUp() {
        getCouponUseCase = new GetCouponUseCase(couponRepositoryPort);
    }

    @Test
    void shouldGetCouponSuccessfully() {

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

        CouponModel result = getCouponUseCase.getCoupon(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getCode()).isEqualTo("ABC123");
        assertThat(result.getDescription()).isEqualTo("Cupom de teste");
        assertThat(result.getDiscountValue())
            .isEqualByComparingTo("0.8");
        assertThat(result.getStatus()).isEqualTo(CouponStatus.ACTIVE);
        assertThat(result.isPublished()).isFalse();
        assertThat(result.isRedeemed()).isFalse();

        verify(couponRepositoryPort, times(1))
            .findById(id);
    }

    @Test
    void shouldThrowExceptionWhenCouponDoesNotExist() {

        UUID id = UUID.randomUUID();

        when(couponRepositoryPort.findById(id))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> getCouponUseCase.getCoupon(id))
            .isInstanceOf(CouponNotFoundException.class)
            .hasMessage("Cupom não encontrado");

        verify(couponRepositoryPort, times(1))
            .findById(id);
    }

    @Test
    void shouldThrowExceptionWhenCouponIsDeleted() {

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

        assertThatThrownBy(() -> getCouponUseCase.getCoupon(id))
            .isInstanceOf(CouponNotFoundException.class)
            .hasMessage("Cupom não encontrado");

        verify(couponRepositoryPort, times(1))
            .findById(id);
    }
}