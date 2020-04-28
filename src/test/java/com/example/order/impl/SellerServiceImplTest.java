package com.example.order.impl;

import com.example.order.dataobject.SellerInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class SellerServiceImplTest {

    private static final String username = "admin";

    @Autowired
    private SellerServiceImpl sellerService;

    @Test
    void findSellerInfoByUsername() {
        SellerInfo result = sellerService.findSellerInfoByUsername(username);
        Assert.assertEquals(username, result.getUsername());
    }
}