package com.testepratico.couponapi.web.controller;

import com.testepratico.couponapi.application.command.CouponCommand;
import com.testepratico.couponapi.application.port.CreateCouponPort;
import com.testepratico.couponapi.application.port.DeleteCouponPort;
import com.testepratico.couponapi.application.port.GetCouponPort;
import com.testepratico.couponapi.domain.model.CouponModel;
import com.testepratico.couponapi.web.contract.CouponControllerContract;
import com.testepratico.couponapi.web.dto.request.CouponRequestDto;
import com.testepratico.couponapi.web.dto.response.CouponResponseDto;
import com.testepratico.couponapi.web.mapper.CouponWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class CouponController implements CouponControllerContract {

    private final CreateCouponPort createCouponPort;
    private final GetCouponPort getCouponPort;
    private final DeleteCouponPort deleteCouponPort;

    @Override
    public ResponseEntity<CouponResponseDto> createCoupon(CouponRequestDto couponRequestDto) {
        CouponCommand couponCommand =  CouponWebMapper.toCommand(couponRequestDto);
        CouponModel couponModel = createCouponPort.createCoupon(couponCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(CouponWebMapper.toResponse(couponModel));
    }

    @Override
    public ResponseEntity<CouponResponseDto> getCoupon(UUID id) {

        CouponModel couponModel =
            getCouponPort.getCoupon(id);

        return ResponseEntity.ok(
            CouponWebMapper.toResponse(couponModel)
        );
    }

    @Override
    public ResponseEntity<Void> deleteCoupon(UUID id) {
        deleteCouponPort.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }
}