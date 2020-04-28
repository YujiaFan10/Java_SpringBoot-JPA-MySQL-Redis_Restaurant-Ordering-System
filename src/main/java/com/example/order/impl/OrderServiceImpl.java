package com.example.order.impl;

import com.example.order.converter.OrderMaster2OrderDTOConverter;
import com.example.order.dataobject.OrderDetail;
import com.example.order.dataobject.OrderMaster;
import com.example.order.dataobject.ProductInfo;
import com.example.order.dto.CartDTO;
import com.example.order.dto.OrderDTO;
import com.example.order.enums.OrderStatusEnum;
import com.example.order.enums.PayStatusEnum;
import com.example.order.enums.ResultEnum;
import com.example.order.exception.OrderException;
import com.example.order.repository.OrderDetailRepository;
import com.example.order.repository.OrderMasterRepository;
import com.example.order.service.OrderService;
import com.example.order.service.ProductService;
import com.example.order.service.WebSocket;
import com.example.order.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private WebSocket webSocket;

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {

        String orderId = KeyUtil.genUniqueKey();
        BigDecimal orderAmount = new BigDecimal(0);

        /** 1. check product (inventory, price) */
        for(OrderDetail orderDetail: orderDTO.getOrderDetailList()){
            ProductInfo productInfo = productService.findOne(orderDetail.getProductId());
            if(productInfo == null){
                throw new OrderException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            /** 2. calculate the total payment amount */
            orderAmount = productInfo.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                    .add(orderAmount);

            /**  write orderDetail into database order_detail,
             * here we only have: productId & productQuantity from the url, and productInfo for others */
            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            orderDetail.setOrderId(orderId);

            /** copy productInfo into orderDetail */
            BeanUtils.copyProperties(productInfo, orderDetail);

            orderDetailRepository.save(orderDetail);
        }

        /** 3. write into the database (order_master & order_detail) */
        OrderMaster orderMaster = new OrderMaster();

        orderDTO.setOrderId(orderId);
        orderDTO.setOrderAmount(orderAmount);
        orderDTO.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderDTO.setOrderStatus(OrderStatusEnum.NEW.getCode());
        BeanUtils.copyProperties(orderDTO, orderMaster);

        orderMasterRepository.save(orderMaster);

        /** 4. reduce the inventory */
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(e ->
                new CartDTO(e.getProductId(), e.getProductQuantity())
                ).collect(Collectors.toList());

        productService.decreaseStock(cartDTOList);

        /** 5. send message via WebSocket */
        webSocket.sendMessage(orderDTO.getOrderId());

        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {
        OrderMaster orderMaster = orderMasterRepository.findById(orderId).orElse(null);
        if(orderMaster == null){
            throw new OrderException(ResultEnum.ORDER_NOT_EXIST);
        }

        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if(CollectionUtils.isEmpty(orderDetailList)){
            throw new OrderException(ResultEnum.ORDER_DETAIL_NOT_EXIST);
        }

        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenId, Pageable pageable) {
        Page<OrderMaster> orderMasterPage=orderMasterRepository.findByBuyerOpenid(buyerOpenId, pageable);

        List<OrderDTO> orderDTOList =  OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());

        return new PageImpl<OrderDTO>(orderDTOList, pageable, orderMasterPage.getTotalElements());
    }

    @Override
    public Page<OrderDTO> findList(Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findAll(pageable);
        List<OrderDTO> orderDTOList =  OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());

        return new PageImpl<OrderDTO>(orderDTOList, pageable, orderMasterPage.getTotalElements());
    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        OrderMaster orderMaster = new OrderMaster();

        // check order status
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("[cancel order] order status is not right, orderId={}, orderStatus={}",
                    orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new OrderException(ResultEnum.ORDER_STATUS_ERROR);
        }

        // change order status
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if(updateResult == null){
            log.error("[cancel order] update fail, orderMaster={}",orderMaster);
            throw new OrderException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        // return stock
        if(CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            log.error("[cancel order] No order detail in the order, orderDTO={}", orderDTO);
            throw new OrderException(ResultEnum.ORDER_DETAIL_LIST_EMPTY);
        }
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().
                map(e -> new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productService.increaseStock(cartDTOList);

        // refund if paid
        if(orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())){
            //TODO
        }
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {
        // check order status
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("[finish order] order status is not right, orderId={}, orderStatus={}",
                    orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new OrderException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //revise order status
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster result = orderMasterRepository.save(orderMaster);
        if(result == null){
            log.error("[finish an order] update failed, orderMaster={}", orderMaster);
            throw new OrderException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {
        //check order status
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("[order payment] order status is not right, orderId={}, orderStatus={}",
                    orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new OrderException(ResultEnum.ORDER_STATUS_ERROR);
        }

        // check payment status
        if(!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())){
            log.error("[payment status is not right] orderId={}, orderDTO={}",
                    orderDTO.getOrderId(), orderDTO.getPayStatus());
            throw new OrderException(ResultEnum.ORDER_PAYMENT_STATUS_ERROR);
        }

        // change payment status
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster result = orderMasterRepository.save(orderMaster);
        if(result == null){
            log.error("[payment status is not right] update failed, orderMaster={}", orderMaster);
            throw new OrderException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        return orderDTO;
    }
}
