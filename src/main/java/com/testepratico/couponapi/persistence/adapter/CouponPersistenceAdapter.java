package com.testepratico.couponapi.persistence.adapter;

import com.testepratico.couponapi.application.port.CouponRepositoryPort;
import com.testepratico.couponapi.domain.model.CouponModel;
import com.testepratico.couponapi.persistence.entity.CouponEntity;
import com.testepratico.couponapi.persistence.mapper.CouponPersistenceMapper;
import com.testepratico.couponapi.persistence.repository.CouponEntityRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class CouponPersistenceAdapter implements CouponRepositoryPort {

    private final CouponEntityRepository couponEntityRepository;

    @Override
    public CouponModel save(CouponModel couponModel) {
        CouponEntity couponEntity = CouponPersistenceMapper.toEntity(couponModel);
        couponEntity = couponEntityRepository.save(couponEntity);
        return CouponPersistenceMapper.toModel(couponEntity);
    }

    @Override
    public Optional<CouponModel> findById(UUID id) {
        return couponEntityRepository.findById(id)
            .map(CouponPersistenceMapper::toModel);
    }
}