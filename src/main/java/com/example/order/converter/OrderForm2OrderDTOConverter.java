package com.example.order.converter;

import com.example.order.dataobject.OrderDetail;
import com.example.order.dto.OrderDTO;
import com.example.order.enums.ResultEnum;
import com.example.order.exception.OrderException;
import com.example.order.form.OrderForm;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OrderForm2OrderDTOConverter {

    public static OrderDTO convert(OrderForm orderForm){
        Gson gson = new Gson();

        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerPhoneNumber(orderForm.getPhone());
        orderDTO.setBuyerOpenid(orderForm.getOpenid());
        orderDTO.setBuyerAddress(orderForm.getAddress());

        List<OrderDetail> orderDetailList = new ArrayList<>();

        //if converting fails, throw exception
        try{
            orderDetailList = gson.fromJson(orderForm.getItems(),
                new TypeToken<List<OrderDetail>>(){}.getType());
        } catch (Exception e){
            log.error("[item convert] convert error, string={}", orderForm.getItems());
            throw new OrderException(ResultEnum.PARAM_ERROR);
        }

        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }

}
