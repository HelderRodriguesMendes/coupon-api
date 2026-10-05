package com.testepratico.couponapi.application.usecase;

import com.testepratico.couponapi.application.port.CouponRepositoryPort;
import com.testepratico.couponapi.application.port.GetCouponPort;
import com.testepratico.couponapi.domain.enums.MessageException;
import com.testepratico.couponapi.domain.exception.CouponNotFoundException;
import com.testepratico.couponapi.domain.model.CouponModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class GetCouponUseCase implements GetCouponPort {

    private final CouponRepositoryPort couponRepositoryPort;

    @Override
    public CouponModel getCoupon(UUID id) {

        log.info("Buscando cupom. id: {}", id);

        CouponModel couponModel = couponRepositoryPort.findById(id)
            .orElseThrow(() ->
                new CouponNotFoundException(
                    MessageException.COUPON_NOT_FOUND.getMessage()
                )
            );

        if (couponModel.isDeleted()) {
            throw new CouponNotFoundException(
                MessageException.COUPON_NOT_FOUND.getMessage()
            );
        }

        log.info("Cupom encontrado. id: {}", id);

        return couponModel;
    }
}