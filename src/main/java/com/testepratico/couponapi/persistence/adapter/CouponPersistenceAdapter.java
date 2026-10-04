package com.testepratico.couponapi.persistence.adapter;

import com.testepratico.couponapi.application.port.CouponRepositoryPort;
import com.testepratico.couponapi.domain.model.CouponModel;
import com.testepratico.couponapi.persistence.entity.CouponEntity;
import com.testepratico.couponapi.persistence.mapper.CouponMapper;
import com.testepratico.couponapi.persistence.repository.CouponEntityRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CouponPersistenceAdapter implements CouponRepositoryPort {

    private final CouponEntityRepository couponEntityRepository;

    @Override
    public CouponModel save(CouponModel couponModel) {
        CouponEntity couponEntity = CouponMapper.toEntity(couponModel);
        couponEntity = couponEntityRepository.save(couponEntity);
        return CouponMapper.toModel(couponEntity);
    }
}