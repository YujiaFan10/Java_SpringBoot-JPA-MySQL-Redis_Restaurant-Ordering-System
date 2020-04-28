package com.example.order.repository;

import com.example.order.dataobject.OrderMaster;
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

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class OrderMasterRepositoryTest {

    @Autowired
    private OrderMasterRepository repository;

    @Test
    @Transactional
    public void saveTest(){
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId("12");
        orderMaster.setBuyerName("Nana");
        orderMaster.setBuyerPhoneNumber("123847908");
        orderMaster.setBuyerAddress("Brooklyn 01, New York");
        orderMaster.setBuyerOpenid("11012");
        orderMaster.setOrderAmount(new BigDecimal((15.2)));

        OrderMaster result = repository.save(orderMaster);
        Assert.assertNotNull(result);
    }

    @Test
    void findByBuyerOpenid() {
        PageRequest request = PageRequest.of(0,1);
        Page<OrderMaster> result = repository.findByBuyerOpenid("11012", request);
        Assert.assertNotEquals(0, result.getTotalElements());
    }
}