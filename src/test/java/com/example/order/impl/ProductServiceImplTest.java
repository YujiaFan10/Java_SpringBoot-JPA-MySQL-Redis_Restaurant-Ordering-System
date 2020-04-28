package com.example.order.impl;

import com.example.order.dataobject.ProductInfo;
import com.example.order.enums.ProductStatusEnum;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ProductServiceImplTest {

    @Autowired
    private ProductServiceImpl productService;

    @Test
    void findOne() {
        ProductInfo productInfo = productService.findOne("100");
        Assert.assertEquals("100", productInfo.getProductId());
    }

    @Test
    void findAllAvailable() {
        List<ProductInfo> productInfoList = productService.findAllAvailable();
        Assert.assertNotEquals(0,productInfoList.size());
    }

    @Test
    void findAll() {
        PageRequest request = PageRequest.of(0,2);
        Page<ProductInfo> productInfoPage = productService.findAll(request);
        //System.out.println(productInfoPage.getTotalElements());
        Assert.assertNotEquals(0,productInfoPage.getTotalElements());
    }

    @Test
    @Transactional
    void save() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("100");
        productInfo.setProductName("Truffle Fries");
        productInfo.setProductPrice(new BigDecimal(5));
        productInfo.setProductStock(20);
        productInfo.setProductDescription("served with ketchup");
        productInfo.setProductIcon("http://xxx.jpg");
        productInfo.setProductStatus(0);
        productInfo.setCategoryType(1);

        ProductInfo result = productService.save(productInfo);
        Assert.assertNotNull(result);
    }

    @Test
    void onSale(){
        ProductInfo result = productService.putOnShelves("100");
        Assert.assertEquals(ProductStatusEnum.UP, result.getProductStatusEnum());
    }

    @Test
    void offSale(){
        ProductInfo result = productService.putOffShelves("100");
        Assert.assertEquals(ProductStatusEnum.DOWN, result.getProductStatusEnum());
    }

}