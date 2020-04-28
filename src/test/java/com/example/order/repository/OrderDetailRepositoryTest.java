package com.example.order.repository;

import com.example.order.dataobject.OrderDetail;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class OrderDetailRepositoryTest {

    @Autowired
    private OrderDetailRepository repository;

    @Test
    @Transactional
    void saveTest(){
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setDetailId("8");
        orderDetail.setOrderId("12");
        orderDetail.setProductIcon("http://xxx.jpg");
        orderDetail.setProductId("100");
        orderDetail.setProductName("Truffle Fries");
        orderDetail.setProductPrice(new BigDecimal(5.00));
        orderDetail.setProductQuantity(1);

        OrderDetail result = repository.save(orderDetail);
        Assert.assertNotNull(result);

    }

    @Test
    void findByOrderId() {
        List<OrderDetail> orderDetailList = repository.findByOrderId("12");
        Assert.assertNotEquals(0,orderDetailList.size());
    }
}