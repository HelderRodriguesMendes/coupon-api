package com.testepratico.couponapi.persistence.repository;

import com.testepratico.couponapi.persistence.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CouponEntityRepository extends JpaRepository<CouponEntity, UUID> {
}