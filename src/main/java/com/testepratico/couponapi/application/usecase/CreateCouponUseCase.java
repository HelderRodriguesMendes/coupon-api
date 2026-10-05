package com.testepratico.couponapi.application.usecase;

import com.testepratico.couponapi.application.command.CouponCommand;
import com.testepratico.couponapi.application.port.CouponRepositoryPort;
import com.testepratico.couponapi.application.port.CreateCouponPort;
import com.testepratico.couponapi.domain.model.CouponModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CreateCouponUseCase implements CreateCouponPort {

    private final CouponRepositoryPort couponRepositoryPort;

    @Override
    public CouponModel createCoupon(CouponCommand couponCommand) {

        log.info("Iniciando criação de cupom com código: {}",
            couponCommand.code());

        CouponModel coupon = CouponModel.create(
                couponCommand.code(),
                couponCommand.description(),
                couponCommand.discountValue(),
                couponCommand.expirationDate(),
                couponCommand.published()
        );

        log.info("Cupom criado com sucesso. id: {}, código: {}",
            coupon.getId(),
            coupon.getCode());

        return couponRepositoryPort.save(coupon);
    }
}