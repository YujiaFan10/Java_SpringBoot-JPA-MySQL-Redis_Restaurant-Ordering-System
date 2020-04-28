package com.example.order.impl;

import com.example.order.dataobject.OrderDetail;
import com.example.order.dto.OrderDTO;
import com.example.order.enums.OrderStatusEnum;
import com.example.order.enums.PayStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderService;

    private final String BUYER_OPENID = "1298990";

    private final String ORDER_ID = "1583464671407842762";

    @Test
    void create() {

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName("Nancy");
        orderDTO.setBuyerPhoneNumber("8237649816");
        orderDTO.setBuyerOpenid(BUYER_OPENID);
        orderDTO.setBuyerAddress("New Brunswick, New Jersey");

        //shopping cart
        List<OrderDetail> orderDetailList = new ArrayList<>();

        OrderDetail o1 = new OrderDetail();
        o1.setProductId("102");
        o1.setProductQuantity(1);

        OrderDetail o2 = new OrderDetail();
        o2.setProductId("100");
        o2.setProductQuantity(2);

        orderDetailList.add(o1);
        orderDetailList.add(o2);

        orderDTO.setOrderDetailList(orderDetailList);
        OrderDTO result = orderService.create(orderDTO);
        log.info("[order created] result={}",result);
        Assert.assertNotNull(result);
    }

    //just search one orderId
    @Test
    void findOne() {
        OrderDTO result = orderService.findOne(ORDER_ID);
        log.info("[search for one order] result ={}", result);
        Assert.assertEquals(ORDER_ID, result.getOrderId());
    }

    //search a list of orderId
    @Test
    void findList() {
        PageRequest request = PageRequest.of(0,2);
        Page<OrderDTO> orderDTOPage = orderService.findList(BUYER_OPENID, request);
        Assert.assertNotEquals(0, orderDTOPage.getTotalElements());
    }

    //search all lists of orderId
    @Test
    void findAllList(){
        PageRequest request = PageRequest.of(0,2);
        Page<OrderDTO> orderDTOPage = orderService.findList(request);
        Assert.assertNotEquals(0, orderDTOPage.getTotalElements());
        //Assert.assertTrue("search all lists of orderId", orderDTOPage.getTotalElements()>0);
    }

    //cancel an order
    @Test
    void cancel() {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        OrderDTO result = orderService.cancel(orderDTO);
        Assert.assertEquals(OrderStatusEnum.CANCEL.getCode(), result.getOrderStatus());
    }

    //finish an order
    @Test
    void finish() {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        OrderDTO result = orderService.finish(orderDTO);
        Assert.assertEquals(OrderStatusEnum.FINISHED.getCode(), result.getOrderStatus());
    }

    @Test
    void paid() {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        OrderDTO result = orderService.paid(orderDTO);
        Assert.assertEquals(PayStatusEnum.SUCCESS.getCode(), result.getPayStatus());
    }
}