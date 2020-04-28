package com.example.order.repository;

import com.example.order.dataobject.SellerInfo;
import com.example.order.utils.KeyUtil;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class SellerInfoRepositoryTest {

    @Autowired
    private SellerInfoRepository repository;

    @Test
    void save(){
        SellerInfo sellerInfo = new SellerInfo();
        sellerInfo.setSellerId(KeyUtil.genUniqueKey());
        sellerInfo.setUsername("admin");
        sellerInfo.setPassword("12345");
        SellerInfo result = repository.save(sellerInfo);
        Assert.assertNotNull(result);
    }

    @Test
    void findByUsername() {
        SellerInfo result = repository.findByUsername("admin");
        Assert.assertEquals("admin", result.getUsername());
    }

}