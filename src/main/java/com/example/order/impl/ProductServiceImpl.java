package com.example.order.impl;

import com.example.order.dataobject.ProductInfo;
import com.example.order.dto.CartDTO;
import com.example.order.enums.ProductStatusEnum;
import com.example.order.enums.ResultEnum;
import com.example.order.exception.OrderException;
import com.example.order.repository.ProductInfoRepository;
import com.example.order.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductInfoRepository repository;

    @Override
    //@Cacheable(cacheNames = "product2", key = "'abc'")
    public ProductInfo findOne(String productId) {
        return repository.findById(productId).orElse(null);
    }

    @Override
    public List<ProductInfo> findAllAvailable() {
        return repository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    //@CachePut(cacheNames = "product2", key = "'abc'")
    public ProductInfo save(ProductInfo productInfo) {
        return repository.save(productInfo);
    }

    @Override
    @Transactional
    public void increaseStock(List<CartDTO> cartDTOList) {
        for(CartDTO cartDTO: cartDTOList){
            ProductInfo productInfo = repository.findById(cartDTO.getProductId()).orElse(null);
            if(productInfo == null){
                throw new OrderException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            Integer result = productInfo.getProductStock() + cartDTO.getProductQuantity();
            productInfo.setProductStock(result);
            repository.save(productInfo);
        }
    }

    @Override
    @Transactional
    public void decreaseStock(List<CartDTO> cartDTOList) {
        for(CartDTO cartDTO: cartDTOList){
            ProductInfo productInfo = repository.findById(cartDTO.getProductId()).orElse(null);
            if(productInfo == null){
                throw new OrderException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            Integer result = productInfo.getProductStock() - cartDTO.getProductQuantity();
            if(result < 0){
                throw new OrderException(ResultEnum.PRODUCT_STOCK_NOT_ENOUGH);
            }
            productInfo.setProductStock(result);
            repository.save(productInfo);
        }
    }

    @Override
    public ProductInfo putOnShelves(String productId) {
        ProductInfo productInfo = repository.findById(productId).orElse(null);
        if(productInfo == null){
            throw new OrderException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        if(productInfo.getProductStatusEnum() == ProductStatusEnum.UP){
            throw new OrderException(ResultEnum.PRODUCT_ALREADY_ON_SHELVES);
        }
        //update
        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
        return repository.save(productInfo);
    }

    @Override
    public ProductInfo putOffShelves(String productId) {
        ProductInfo productInfo = repository.findById(productId).orElse(null);
        if(productInfo == null){
            throw new OrderException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        if(productInfo.getProductStatusEnum() == ProductStatusEnum.DOWN){
            throw new OrderException(ResultEnum.PRODUCT_ALREADY_OFF_SHELVES);
        }
        //update
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
        return repository.save(productInfo);
    }
}
