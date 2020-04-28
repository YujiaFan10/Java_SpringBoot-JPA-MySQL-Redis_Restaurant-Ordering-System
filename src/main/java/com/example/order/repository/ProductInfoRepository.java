package com.example.order.repository;

import com.example.order.dataobject.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductInfoRepository extends JpaRepository<ProductInfo, String> {

    /** find all available products. */
    List<ProductInfo> findByProductStatus(Integer productStatus);
}
