package com.example.order.dto;

import com.example.order.dataobject.OrderDetail;
import com.example.order.enums.OrderStatusEnum;
import com.example.order.enums.PayStatusEnum;
import com.example.order.utils.EnumUtil;
import com.example.order.utils.serializer.Date2LongSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {

    private  String orderId;

    private String buyerName;

    private String buyerPhoneNumber;

    private String buyerAddress;

    /** Wechat openid for buyer */
    private String buyerOpenid;

    private BigDecimal orderAmount;

    /** build OrderStatusEnum, default is 0, order placed */
    private Integer orderStatus ;

    /** build PayStatusEnum, default is 0, unpaid */
    private Integer payStatus;

    /** create time for the order */
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;

    /** update time for the order */
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;

    /** the same orderMaster corresponds to multiple orderDetail **/
    private List<OrderDetail> orderDetailList;

    @JsonIgnore
    public OrderStatusEnum getOrderStatusEnum(){
        return EnumUtil.getByCode(orderStatus, OrderStatusEnum.class);
    }

    @JsonIgnore
    public PayStatusEnum getPayStatusEnum(){
        return EnumUtil.getByCode(payStatus, PayStatusEnum.class);
    }
}
