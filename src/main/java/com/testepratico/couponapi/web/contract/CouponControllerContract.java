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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Coupon API", description = "API for managing coupons")

@RequestMapping("/coupon")
public interface CouponControllerContract {

    @Operation(summary = "Create a new coupon")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = CouponResponseDto.class))},
            description = "Coupon created successfully"),
    })
    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<CouponResponseDto> createCoupon(@Valid @RequestBody CouponRequestDto couponRequestDto);

    @GetMapping(
        value = "/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
        summary = "Busca um cupom",
        description = "Busca um cupom pelo identificador"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Cupom encontrado"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Cupom não encontrado"
        )
    })
    ResponseEntity<CouponResponseDto> getCoupon(
        @PathVariable UUID id
    );
}