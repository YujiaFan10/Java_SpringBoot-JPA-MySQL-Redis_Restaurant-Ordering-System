package com.example.order.service;

import com.example.order.dto.OrderDTO;

public interface BuyerService {

    // search an order
    OrderDTO findOrderOne(String openid, String orderId);

    //cancel an order
    OrderDTO cancelOrder(String openid, String orderId);


}
