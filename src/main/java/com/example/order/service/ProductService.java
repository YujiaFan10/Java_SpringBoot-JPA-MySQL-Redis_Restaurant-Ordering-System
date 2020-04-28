package com.example.order.service;

import com.example.order.dataobject.ProductInfo;
import com.example.order.dto.CartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    ProductInfo findOne(String productId);

    List<ProductInfo> findAllAvailable();

    Page<ProductInfo> findAll(Pageable pageable);

    ProductInfo save(ProductInfo productInfo);

    //add inventory
    void increaseStock (List<CartDTO> cartDTOList);

    //reduce inventory
    void decreaseStock (List<CartDTO> cartDTOList);

    //put on shelves
    ProductInfo putOnShelves(String productId);

    //put off shelves
    ProductInfo putOffShelves(String productId);

}
