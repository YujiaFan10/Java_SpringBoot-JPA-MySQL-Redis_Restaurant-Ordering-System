package com.example.order.dataobject;

import com.example.order.enums.OrderStatusEnum;
import com.example.order.enums.PayStatusEnum;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Data
@DynamicUpdate
public class OrderMaster {

    @Id
    private  String orderId;

    private String buyerName;

    private String buyerPhoneNumber;

    private String buyerAddress;

    /** Wechat openid for buyer */
    private String buyerOpenid;

    private BigDecimal orderAmount;

    /** build OrderStatusEnum, default is 0, order placed */
    private Integer orderStatus = OrderStatusEnum.NEW.getCode();

    /** build PayStatusEnum, default is 0, unpaid */
    private Integer payStatus = PayStatusEnum.WAIT.getCode();

    /** create time for the order */
    private Date createTime;

    /** update time for the order */
    private Date updateTime;

    /** the same orderMaster corresponds to multiple orderDetail **/
    //@Transient  //orderDetailList should be ignored by the database
    //private List<OrderDetail> orderDetailList;
    //Using Transient is not straightforward in code, use dto instead

}
