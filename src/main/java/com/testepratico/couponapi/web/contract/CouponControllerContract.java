package com.testepratico.couponapi.web.contract;


import com.testepratico.couponapi.web.dto.request.CouponRequestDto;
import com.testepratico.couponapi.web.dto.response.CouponResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Coupon API", description = "API for managing coupons")

@RequestMapping("/coupon")
public interface CouponControllerContract {
    String MEDIA_TYPE = "application/json";


    @Operation(summary = "Create a new coupon")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201",
            content = {@Content(mediaType = MEDIA_TYPE,
            schema = @Schema(implementation = CouponResponseDto.class))},
            description = "Coupon created successfully"),
    })
    @PostMapping(
        consumes = MEDIA_TYPE,
        produces = MEDIA_TYPE
    )
    ResponseEntity<CouponResponseDto> createCoupon(@Valid @RequestBody CouponRequestDto couponRequestDto);
}