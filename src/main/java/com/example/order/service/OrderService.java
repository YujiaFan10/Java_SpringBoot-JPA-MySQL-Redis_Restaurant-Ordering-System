package com.example.order.service;

import com.example.order.dataobject.OrderMaster;
import com.example.order.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    /** creat an order */
    OrderDTO create(OrderDTO orderDTO);

    /** search for an order */
    OrderDTO findOne(String orderId);

    /** search for an order list */
    Page<OrderDTO> findList(String buyerOpenId, Pageable pageable);

    /** cancel an order */
    OrderDTO cancel(OrderDTO orderDTO);

    /** complete an order */
    OrderDTO finish(OrderDTO orderDTO);

    /** pay for an order */
    OrderDTO paid(OrderDTO orderDTO);

    /** search for all order lists */
    Page<OrderDTO> findList(Pageable pageable);

}
