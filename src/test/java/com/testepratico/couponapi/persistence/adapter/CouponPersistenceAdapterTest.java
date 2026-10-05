package com.testepratico.couponapi.persistence.adapter;

import com.testepratico.couponapi.domain.enums.CouponStatus;
import com.testepratico.couponapi.domain.model.CouponModel;
import com.testepratico.couponapi.persistence.entity.CouponEntity;
import com.testepratico.couponapi.persistence.repository.CouponEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponPersistenceAdapterTest {

    @Mock
    private CouponEntityRepository couponEntityRepository;

    private CouponPersistenceAdapter couponPersistenceAdapter;

    @BeforeEach
    void setUp() {
        couponPersistenceAdapter =
            new CouponPersistenceAdapter(couponEntityRepository);
    }

    @Test
    void shouldSaveCouponSuccessfully() {
        Instant expirationDate = Instant.now().plusSeconds(3600);

        CouponModel couponModel = CouponModel.create(
            "ABC123",
            "Cupom de teste",
            new BigDecimal("10.00"),
            expirationDate,
            false
        );

        when(couponEntityRepository.save(any(CouponEntity.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        CouponModel result =
            couponPersistenceAdapter.save(couponModel);

        assertNotNull(result);
        assertEquals(couponModel.getId(), result.getId());
        assertEquals("ABC123", result.getCode());
        assertEquals("Cupom de teste", result.getDescription());
        assertEquals(new BigDecimal("10.00"), result.getDiscountValue());
        assertEquals(expirationDate, result.getExpirationDate());
        assertEquals(CouponStatus.ACTIVE, result.getStatus());
        assertFalse(result.isPublished());
        assertFalse(result.isRedeemed());

        verify(couponEntityRepository, times(1))
            .save(any(CouponEntity.class));
    }

    @Test
    void shouldSendCouponEntityToRepository() {
        CouponModel couponModel = CouponModel.create(
            "ABC123",
            "Cupom de teste",
            new BigDecimal("5.00"),
            Instant.now().plusSeconds(3600),
            true
        );

        when(couponEntityRepository.save(any(CouponEntity.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        couponPersistenceAdapter.save(couponModel);

        ArgumentCaptor<CouponEntity> captor =
            ArgumentCaptor.forClass(CouponEntity.class);

        verify(couponEntityRepository).save(captor.capture());

        CouponEntity savedEntity = captor.getValue();

        assertEquals(couponModel.getId(), savedEntity.getId());
        assertEquals(couponModel.getCode(), savedEntity.getCode());
        assertEquals(couponModel.getDescription(), savedEntity.getDescription());
        assertEquals(couponModel.getDiscountValue(), savedEntity.getDiscountValue());
        assertEquals(couponModel.getExpirationDate(), savedEntity.getExpirationDate());
        assertEquals(CouponStatus.ACTIVE, savedEntity.getStatus());
        assertTrue(savedEntity.isPublished());
        assertFalse(savedEntity.isRedeemed());
    }

    @Test
    void shouldFindCouponByIdSuccessfully() {

        UUID id = UUID.randomUUID();

        CouponEntity couponEntity = CouponEntity.builder()
            .id(id)
            .code("ABC123")
            .description("Cupom de teste")
            .discountValue(new BigDecimal("0.8"))
            .expirationDate(Instant.parse("2099-12-31T23:59:59Z"))
            .status(CouponStatus.ACTIVE)
            .published(false)
            .redeemed(false)
            .build();

        when(couponEntityRepository.findById(id))
            .thenReturn(Optional.of(couponEntity));

        Optional<CouponModel> result =
            couponPersistenceAdapter.findById(id);

        assertThat(result).isPresent();

        CouponModel couponModel = result.get();

        assertThat(couponModel.getId()).isEqualTo(id);
        assertThat(couponModel.getCode()).isEqualTo("ABC123");
        assertThat(couponModel.getDescription())
            .isEqualTo("Cupom de teste");
        assertThat(couponModel.getDiscountValue())
            .isEqualByComparingTo("0.8");
        assertThat(couponModel.getStatus())
            .isEqualTo(CouponStatus.ACTIVE);

        verify(couponEntityRepository, times(1))
            .findById(id);
    }

    @Test
    void shouldReturnEmptyWhenCouponDoesNotExist() {

        UUID id = UUID.randomUUID();

        when(couponEntityRepository.findById(id))
            .thenReturn(Optional.empty());

        Optional<CouponModel> result =
            couponPersistenceAdapter.findById(id);

        assertThat(result).isEmpty();

        verify(couponEntityRepository, times(1))
            .findById(id);
    }
}