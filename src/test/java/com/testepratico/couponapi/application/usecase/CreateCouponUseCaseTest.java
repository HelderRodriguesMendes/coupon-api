package com.testepratico.couponapi.application.usecase;

import com.testepratico.couponapi.application.command.CouponCommand;
import com.testepratico.couponapi.application.port.CouponRepositoryPort;
import com.testepratico.couponapi.domain.enums.CouponStatus;
import com.testepratico.couponapi.domain.model.CouponModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCouponUseCaseTest {

    @Mock
    private CouponRepositoryPort couponRepositoryPort;

    private CreateCouponUseCase createCouponUseCase;

    @BeforeEach
    void setUp() {
        createCouponUseCase = new CreateCouponUseCase(couponRepositoryPort);
    }

    @Test
    void shouldCreateCouponSuccessfully() {
        CouponCommand command = new CouponCommand(
            "ABC123",
            "Cupom de teste",
            new BigDecimal("10.00"),
            Instant.now().plusSeconds(3600),
            false
        );

        when(couponRepositoryPort.save(any(CouponModel.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        CouponModel result = createCouponUseCase.createCoupon(command);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("ABC123", result.getCode());
        assertEquals("Cupom de teste", result.getDescription());
        assertEquals(new BigDecimal("10.00"), result.getDiscountValue());
        assertEquals(CouponStatus.ACTIVE, result.getStatus());
        assertFalse(result.isPublished());
        assertFalse(result.isRedeemed());

        verify(couponRepositoryPort, times(1))
            .save(any(CouponModel.class));
    }

    @Test
    void shouldCreatePublishedCouponSuccessfully() {
        CouponCommand command = new CouponCommand(
            "ABC123",
            "Cupom publicado",
            new BigDecimal("5.00"),
            Instant.now().plusSeconds(3600),
            true
        );

        when(couponRepositoryPort.save(any(CouponModel.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        CouponModel result = createCouponUseCase.createCoupon(command);

        assertNotNull(result);
        assertTrue(result.isPublished());
        assertEquals(CouponStatus.ACTIVE, result.getStatus());

        verify(couponRepositoryPort, times(1))
            .save(any(CouponModel.class));
    }

    @Test
    void shouldSendCreatedCouponToRepository() {
        CouponCommand command = new CouponCommand(
            "ABC123",
            "Cupom de teste",
            new BigDecimal("5.00"),
            Instant.now().plusSeconds(3600),
            true
        );

        when(couponRepositoryPort.save(any(CouponModel.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        createCouponUseCase.createCoupon(command);

        ArgumentCaptor<CouponModel> captor =
            ArgumentCaptor.forClass(CouponModel.class);

        verify(couponRepositoryPort).save(captor.capture());

        CouponModel savedCoupon = captor.getValue();

        assertNotNull(savedCoupon.getId());
        assertEquals("ABC123", savedCoupon.getCode());
        assertEquals("Cupom de teste", savedCoupon.getDescription());
        assertEquals(new BigDecimal("5.00"), savedCoupon.getDiscountValue());
        assertEquals(CouponStatus.ACTIVE, savedCoupon.getStatus());
        assertTrue(savedCoupon.isPublished());
        assertFalse(savedCoupon.isRedeemed());
    }

    @Test
    void shouldSanitizeCodeBeforeSavingCoupon() {
        CouponCommand command = new CouponCommand(
            "ABC-123",
            "Cupom de teste",
            new BigDecimal("10.00"),
            Instant.now().plusSeconds(3600),
            false
        );

        when(couponRepositoryPort.save(any(CouponModel.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        createCouponUseCase.createCoupon(command);

        ArgumentCaptor<CouponModel> captor =
            ArgumentCaptor.forClass(CouponModel.class);

        verify(couponRepositoryPort).save(captor.capture());

        CouponModel savedCoupon = captor.getValue();

        assertEquals("ABC123", savedCoupon.getCode());
    }
}