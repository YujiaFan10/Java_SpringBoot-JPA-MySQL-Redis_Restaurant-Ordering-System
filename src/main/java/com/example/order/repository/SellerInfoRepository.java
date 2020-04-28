package com.example.order.repository;

import com.example.order.dataobject.SellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerInfoRepository extends JpaRepository<SellerInfo, String> {
    SellerInfo findByUsername(String username);
}
