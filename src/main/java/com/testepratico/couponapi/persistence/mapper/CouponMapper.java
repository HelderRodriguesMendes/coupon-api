package com.testepratico.couponapi.persistence.mapper;

import com.testepratico.couponapi.domain.model.CouponModel;
import com.testepratico.couponapi.persistence.entity.CouponEntity;

public final class CouponMapper {

    public static CouponEntity toEntity(CouponModel couponModel) {
        return CouponEntity.builder()
                .id(couponModel.getId())
                .code(couponModel.getCode())
                .description(couponModel.getDescription())
                .discountValue(couponModel.getDiscountValue())
                .expirationDate(couponModel.getExpirationDate())
                .status(couponModel.getStatus())
                .published(couponModel.isPublished())
                .redeemed(couponModel.isRedeemed())
                .build();
    }

    public static CouponModel toModel(CouponEntity couponEntity) {
        return CouponModel.restore(
                couponEntity.getId(),
                couponEntity.getCode(),
                couponEntity.getDescription(),
                couponEntity.getDiscountValue(),
                couponEntity.getExpirationDate(),
                couponEntity.getStatus(),
                couponEntity.isPublished(),
                couponEntity.isRedeemed()
        );
    }
}