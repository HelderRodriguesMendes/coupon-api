package com.testepratico.couponapi.integration;

import com.testepratico.couponapi.domain.enums.CouponStatus;
import com.testepratico.couponapi.persistence.entity.CouponEntity;
import com.testepratico.couponapi.persistence.repository.CouponEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CouponDeleteIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CouponEntityRepository couponEntityRepository;

    @BeforeEach
    void setUp() {
        couponEntityRepository.deleteAll();
    }

    @Test
    void shouldDeleteCouponSuccessfully() throws Exception {

        UUID id = UUID.randomUUID();

        CouponEntity couponEntity = createCouponEntity(
            id,
            CouponStatus.ACTIVE
        );

        couponEntityRepository.save(couponEntity);

        mockMvc.perform(delete("/coupon/{id}", id))
            .andExpect(status().isNoContent());

        CouponEntity deletedCoupon =
            couponEntityRepository.findById(id)
                .orElseThrow();

        assertThat(deletedCoupon.getId())
            .isEqualTo(id);

        assertThat(deletedCoupon.getStatus())
            .isEqualTo(CouponStatus.DELETED);
    }

    @Test
    void shouldKeepCouponInDatabaseAfterDelete() throws Exception {

        UUID id = UUID.randomUUID();

        CouponEntity couponEntity = createCouponEntity(
            id,
            CouponStatus.ACTIVE
        );

        couponEntityRepository.save(couponEntity);

        mockMvc.perform(delete("/coupon/{id}", id))
            .andExpect(status().isNoContent());

        assertThat(couponEntityRepository.existsById(id))
            .isTrue();

        assertThat(couponEntityRepository.count())
            .isEqualTo(1);
    }

    @Test
    void shouldPreserveOriginalDataAfterDelete() throws Exception {

        UUID id = UUID.randomUUID();

        CouponEntity couponEntity = createCouponEntity(
            id,
            CouponStatus.ACTIVE
        );

        couponEntityRepository.save(couponEntity);

        mockMvc.perform(delete("/coupon/{id}", id))
            .andExpect(status().isNoContent());

        CouponEntity deletedCoupon =
            couponEntityRepository.findById(id)
                .orElseThrow();

        assertThat(deletedCoupon.getId())
            .isEqualTo(id);

        assertThat(deletedCoupon.getCode())
            .isEqualTo("ABC123");

        assertThat(deletedCoupon.getDescription())
            .isEqualTo("Cupom de teste");

        assertThat(deletedCoupon.getDiscountValue())
            .isEqualByComparingTo("0.8");

        assertThat(deletedCoupon.isPublished())
            .isFalse();

        assertThat(deletedCoupon.isRedeemed())
            .isFalse();

        assertThat(deletedCoupon.getStatus())
            .isEqualTo(CouponStatus.DELETED);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingCoupon()
        throws Exception {

        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/coupon/{id}", id))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message")
                .value("Cupom não encontrado"));

        assertThat(couponEntityRepository.count())
            .isZero();
    }

    @Test
    void shouldNotDeleteCouponTwice() throws Exception {

        UUID id = UUID.randomUUID();

        CouponEntity couponEntity = createCouponEntity(
            id,
            CouponStatus.ACTIVE
        );

        couponEntityRepository.save(couponEntity);

        // Primeira exclusão
        mockMvc.perform(delete("/coupon/{id}", id))
            .andExpect(status().isNoContent());

        // Segunda exclusão
        mockMvc.perform(delete("/coupon/{id}", id))
            .andExpect(status().isBadRequest());

        CouponEntity deletedCoupon =
            couponEntityRepository.findById(id)
                .orElseThrow();

        assertThat(deletedCoupon.getStatus())
            .isEqualTo(CouponStatus.DELETED);

        assertThat(couponEntityRepository.count())
            .isEqualTo(1);
    }

    @Test
    void shouldNotReturnCouponAfterDelete() throws Exception {

        UUID id = UUID.randomUUID();

        CouponEntity couponEntity = createCouponEntity(
            id,
            CouponStatus.ACTIVE
        );

        couponEntityRepository.save(couponEntity);

        mockMvc.perform(delete("/coupon/{id}", id))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/coupon/{id}", id))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message")
                .value("Cupom não encontrado"));

        // Apesar do GET retornar 404, o registro continua no banco.
        assertThat(couponEntityRepository.existsById(id))
            .isTrue();

        CouponEntity deletedCoupon =
            couponEntityRepository.findById(id)
                .orElseThrow();

        assertThat(deletedCoupon.getStatus())
            .isEqualTo(CouponStatus.DELETED);
    }

    private CouponEntity createCouponEntity(
        UUID id,
        CouponStatus status) {

        return CouponEntity.builder()
            .id(id)
            .code("ABC123")
            .description("Cupom de teste")
            .discountValue(new BigDecimal("0.8"))
            .expirationDate(
                Instant.parse("2099-12-31T23:59:59Z")
            )
            .status(status)
            .published(false)
            .redeemed(false)
            .build();
    }
}