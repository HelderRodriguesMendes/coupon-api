package com.testepratico.couponapi.web.mapper;

import com.testepratico.couponapi.application.command.CouponCommand;
import com.testepratico.couponapi.domain.model.CouponModel;
import com.testepratico.couponapi.web.dto.request.CouponRequestDto;
import com.testepratico.couponapi.web.dto.response.CouponResponseDto;

public final class CouponWebMapper {

    public static CouponCommand toCommand(CouponRequestDto request) {
        return new CouponCommand(
            request.code(),
            request.description(),
            request.discountValue(),
            request.expirationDate(),
            request.published()
        );
    }

    public static CouponResponseDto toResponse(CouponModel couponModel){
        return new CouponResponseDto(
            couponModel.getId(),
            couponModel.getCode(),
            couponModel.getDescription(),
            couponModel.getDiscountValue(),
            couponModel.getExpirationDate(),
            couponModel.isPublished(),
            couponModel.getStatus(),
            couponModel.isRedeemed()
        );
    }
}