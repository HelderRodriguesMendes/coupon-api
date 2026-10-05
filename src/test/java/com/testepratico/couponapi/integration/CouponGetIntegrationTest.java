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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CouponGetIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CouponEntityRepository couponEntityRepository;

    @BeforeEach
    void setUp() {
        couponEntityRepository.deleteAll();
    }

    @Test
    void shouldGetCouponByIdSuccessfully() throws Exception {

        UUID id = UUID.randomUUID();

        CouponEntity couponEntity = CouponEntity.builder()
            .id(id)
            .code("ABC123")
            .description("Cupom de teste")
            .discountValue(new BigDecimal("0.8"))
            .expirationDate(
                Instant.parse("2099-12-31T23:59:59Z")
            )
            .status(CouponStatus.ACTIVE)
            .published(false)
            .redeemed(false)
            .build();

        couponEntityRepository.save(couponEntity);

        mockMvc.perform(get("/coupon/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id.toString()))
            .andExpect(jsonPath("$.code").value("ABC123"))
            .andExpect(jsonPath("$.description")
                .value("Cupom de teste"))
            .andExpect(jsonPath("$.discountValue").value(0.8))
            .andExpect(jsonPath("$.status").value("ACTIVE"))
            .andExpect(jsonPath("$.published").value(false))
            .andExpect(jsonPath("$.redeemed").value(false));

        assertThat(
            couponEntityRepository.findById(id)
        ).isPresent();
    }

    @Test
    void shouldReturnNotFoundWhenCouponDoesNotExist()
        throws Exception {

        UUID id = UUID.randomUUID();

        mockMvc.perform(get("/coupon/{id}", id))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message")
                .value("Cupom não encontrado"));

        assertThat(couponEntityRepository.count()).isZero();
    }
}