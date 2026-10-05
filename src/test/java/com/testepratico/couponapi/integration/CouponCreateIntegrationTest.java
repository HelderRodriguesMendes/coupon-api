package com.testepratico.couponapi.integration;

import com.testepratico.couponapi.persistence.repository.CouponEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CouponCreateIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CouponEntityRepository couponEntityRepository;

    @BeforeEach
    void setUp() {
        couponEntityRepository.deleteAll();
    }

    @Test
    void shouldCreateCouponAndPersistInH2() throws Exception {

        String payload = """
                {
                    "code": "ABC-123",
                    "description": "Cupom de desconto para teste",
                    "discountValue": 0.8,
                    "expirationDate": "2026-12-31T23:59:59Z",
                    "published": false
                }
                """;

        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.code").value("ABC123"))
            .andExpect(jsonPath("$.description")
                .value("Cupom de desconto para teste"))
            .andExpect(jsonPath("$.discountValue").value(0.8))
            .andExpect(jsonPath("$.status").value("ACTIVE"))
            .andExpect(jsonPath("$.published").value(false))
            .andExpect(jsonPath("$.redeemed").value(false));

        var coupons = couponEntityRepository.findAll();

        assertThat(coupons).hasSize(1);

        var coupon = coupons.getFirst();

        assertThat(coupon.getId()).isNotNull();
        assertThat(coupon.getCode()).isEqualTo("ABC123");
        assertThat(coupon.getDescription())
            .isEqualTo("Cupom de desconto para teste");
        assertThat(coupon.getDiscountValue())
            .isEqualByComparingTo("0.8");
        assertThat(coupon.isPublished()).isFalse();
        assertThat(coupon.isRedeemed()).isFalse();
    }

    @Test
    void shouldNotPersistCouponWhenCodeIsInvalid() throws Exception {

        String payload = """
                {
                    "code": "ABCSFDS-12398",
                    "description": "Cupom de desconto para teste",
                    "discountValue": 0.8,
                    "expirationDate": "2026-12-31T23:59:59Z",
                    "published": false
                }
                """;

        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message")
                .value("O código do cupom deve conter exatamente 6 caracteres alfanuméricos"))
            .andExpect(jsonPath("$.errors").doesNotExist());

        assertThat(couponEntityRepository.count()).isZero();
    }

    @Test
    void shouldNotPersistCouponWhenDiscountValueIsInvalid() throws Exception {

        String payload = """
                {
                    "code": "ABC-123",
                    "description": "Cupom de desconto para teste",
                    "discountValue": 0.4,
                    "expirationDate": "2026-12-31T23:59:59Z",
                    "published": false
                }
                """;

        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message")
                .value("O valor do desconto deve ser no mínimo 0.5"));

        assertThat(couponEntityRepository.count()).isZero();
    }

    @Test
    void shouldNotPersistCouponWhenExpirationDateIsInThePast() throws Exception {

        String payload = """
                {
                    "code": "ABC-123",
                    "description": "Cupom de desconto para teste",
                    "discountValue": 0.8,
                    "expirationDate": "2020-01-01T00:00:00Z",
                    "published": false
                }
                """;

        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message")
                .value("A data de expiração do cupom não pode estar no passado."));

        assertThat(couponEntityRepository.count()).isZero();
    }
}