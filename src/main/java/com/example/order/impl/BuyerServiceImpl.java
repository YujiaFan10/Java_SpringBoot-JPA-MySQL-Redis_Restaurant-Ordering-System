package com.example.order.impl;

import com.example.order.dto.OrderDTO;
import com.example.order.enums.ResultEnum;
import com.example.order.exception.OrderException;
import com.example.order.service.BuyerService;
import com.example.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BuyerServiceImpl implements BuyerService {

    @Autowired
    private OrderService orderService;

    @Override
    public OrderDTO findOrderOne(String openid, String orderId) {
        OrderDTO orderDTO = checkOrderOwner(openid, orderId);
        return orderDTO;
    }

    @Override
    public OrderDTO cancelOrder(String openid, String orderId) {
        OrderDTO orderDTO = checkOrderOwner(openid, orderId);
        if(orderDTO == null){
            log.error("[cancel an order] No such order, orderId={}, orderDTO={}", orderId, orderDTO);
            throw new OrderException(ResultEnum.ORDER_NOT_EXIST);
        }
        return orderService.cancel(orderDTO);
    }

    private OrderDTO checkOrderOwner(String openid, String orderId){
        OrderDTO orderDTO = orderService.findOne(orderId);
        if(orderDTO == null) return null;
        //check if the order is owned by the owner
        if(!orderDTO.getBuyerOpenid().equalsIgnoreCase(openid)){
            log.error("[search an order] Inconsistency of openid. openid={}, orderDTO={}", openid, orderDTO);
            throw new OrderException(ResultEnum.ORDER_OWNER_ERROR);
        }
        return orderDTO;
    }
}
