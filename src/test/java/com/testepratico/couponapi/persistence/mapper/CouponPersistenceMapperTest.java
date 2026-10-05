package com.testepratico.couponapi.persistence.mapper;

import com.testepratico.couponapi.domain.enums.CouponStatus;
import com.testepratico.couponapi.domain.model.CouponModel;
import com.testepratico.couponapi.persistence.entity.CouponEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CouponPersistenceMapperTest {

    @Test
    void shouldConvertCouponModelToCouponEntity() {
        Instant expirationDate = Instant.now().plusSeconds(3600);

        CouponModel couponModel = CouponModel.create(
            "ABC123",
            "Cupom de teste",
            new BigDecimal("10.00"),
            expirationDate,
            true
        );

        CouponEntity couponEntity = CouponPersistenceMapper.toEntity(couponModel);

        assertNotNull(couponEntity);
        assertEquals(couponModel.getId(), couponEntity.getId());
        assertEquals(couponModel.getCode(), couponEntity.getCode());
        assertEquals(couponModel.getDescription(), couponEntity.getDescription());
        assertEquals(couponModel.getDiscountValue(), couponEntity.getDiscountValue());
        assertEquals(couponModel.getExpirationDate(), couponEntity.getExpirationDate());
        assertEquals(couponModel.getStatus(), couponEntity.getStatus());
        assertEquals(couponModel.isPublished(), couponEntity.isPublished());
        assertEquals(couponModel.isRedeemed(), couponEntity.isRedeemed());
    }

    @Test
    void shouldConvertCouponEntityToCouponModel() {
        UUID id = UUID.randomUUID();
        Instant expirationDate = Instant.now().plusSeconds(3600);

        CouponEntity couponEntity = CouponEntity.builder()
            .id(id)
            .code("ABC123")
            .description("Cupom de teste")
            .discountValue(new BigDecimal("10.00"))
            .expirationDate(expirationDate)
            .status(CouponStatus.ACTIVE)
            .published(true)
            .redeemed(false)
            .build();

        CouponModel couponModel = CouponPersistenceMapper.toModel(couponEntity);

        assertNotNull(couponModel);
        assertEquals(id, couponModel.getId());
        assertEquals("ABC123", couponModel.getCode());
        assertEquals("Cupom de teste", couponModel.getDescription());
        assertEquals(new BigDecimal("10.00"), couponModel.getDiscountValue());
        assertEquals(expirationDate, couponModel.getExpirationDate());
        assertEquals(CouponStatus.ACTIVE, couponModel.getStatus());
        assertTrue(couponModel.isPublished());
        assertFalse(couponModel.isRedeemed());
    }

    @Test
    void shouldPreserveDeletedStatusWhenConvertingEntityToModel() {
        UUID id = UUID.randomUUID();

        CouponEntity couponEntity = CouponEntity.builder()
            .id(id)
            .code("ABC123")
            .description("Cupom deletado")
            .discountValue(new BigDecimal("10.00"))
            .expirationDate(Instant.now().plusSeconds(3600))
            .status(CouponStatus.DELETED)
            .published(true)
            .redeemed(false)
            .build();

        CouponModel couponModel = CouponPersistenceMapper.toModel(couponEntity);

        assertNotNull(couponModel);
        assertEquals(id, couponModel.getId());
        assertEquals(CouponStatus.DELETED, couponModel.getStatus());
    }

    @Test
    void shouldPreserveRedeemedStatusWhenConvertingEntityToModel() {
        UUID id = UUID.randomUUID();

        CouponEntity couponEntity = CouponEntity.builder()
            .id(id)
            .code("ABC123")
            .description("Cupom resgatado")
            .discountValue(new BigDecimal("10.00"))
            .expirationDate(Instant.now().plusSeconds(3600))
            .status(CouponStatus.ACTIVE)
            .published(true)
            .redeemed(true)
            .build();

        CouponModel couponModel = CouponPersistenceMapper.toModel(couponEntity);

        assertNotNull(couponModel);
        assertEquals(id, couponModel.getId());
        assertTrue(couponModel.isRedeemed());
    }
}