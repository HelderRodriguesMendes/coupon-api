package com.testepratico.couponapi.config;

import com.testepratico.couponapi.application.port.CouponRepositoryPort;
import com.testepratico.couponapi.application.port.CreateCouponPort;
import com.testepratico.couponapi.application.port.DeleteCouponPort;
import com.testepratico.couponapi.application.port.GetCouponPort;
import com.testepratico.couponapi.application.usecase.CreateCouponUseCase;
import com.testepratico.couponapi.application.usecase.DeleteCouponUseCase;
import com.testepratico.couponapi.application.usecase.GetCouponUseCase;
import com.testepratico.couponapi.persistence.adapter.CouponPersistenceAdapter;
import com.testepratico.couponapi.persistence.repository.CouponEntityRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CouponConfig {

    @Bean
    public CouponRepositoryPort couponRepositoryPort(
        CouponEntityRepository couponEntityRepository) {

        return new CouponPersistenceAdapter(couponEntityRepository);
    }

    @Bean
    public CreateCouponPort createCouponPort(
        CouponRepositoryPort couponRepositoryPort) {

        return new CreateCouponUseCase(couponRepositoryPort);
    }

    @Bean
    public GetCouponPort getCouponPort(
        CouponRepositoryPort couponRepositoryPort) {

        return new GetCouponUseCase(
            couponRepositoryPort
        );
    }

    @Bean
    public DeleteCouponPort deleteCouponPort(
        CouponRepositoryPort couponRepositoryPort) {

        return new DeleteCouponUseCase(couponRepositoryPort);
    }
}