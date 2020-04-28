package com.example.order.controller;

import com.example.order.VO.ResultVO;
import com.example.order.converter.OrderForm2OrderDTOConverter;
import com.example.order.dto.OrderDTO;
import com.example.order.enums.ResultEnum;
import com.example.order.exception.OrderException;
import com.example.order.form.OrderForm;
import com.example.order.service.BuyerService;
import com.example.order.service.OrderService;
import com.example.order.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BuyerService buyerService;

    /** 1. create order */
    @PostMapping("/create")
    public ResultVO<Map<String, String>> create(@Valid OrderForm orderForm,
                                                BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            log.error("[Create order] wrong parameter, orderForm={}",orderForm);
            throw new OrderException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }
        OrderDTO orderDTO = OrderForm2OrderDTOConverter.convert(orderForm);
        if(CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            log.error("[create order] shopping cart cannot be empty");
            throw new OrderException(ResultEnum.CART_EMPTY);
        }
        OrderDTO creatResult = orderService.create(orderDTO);

        Map<String, String> map = new HashMap<>();
        map.put("orderId", creatResult.getOrderId());
        return ResultVOUtil.success(map);
    }

    /** 2. order list
     * find order details according to the openid
     * */
    @GetMapping("/list")
    public ResultVO<List<OrderDTO>> list(@RequestParam("openid") String openid,
                                         @RequestParam(value = "page", defaultValue = "0") Integer page,
                                         @RequestParam(value = "size", defaultValue = "10") Integer size){
        if(StringUtils.isEmpty(openid)){
            log.error("[search order detail list] openid is missed");
            throw new OrderException(ResultEnum.PARAM_ERROR);
        }

        PageRequest request = PageRequest.of(page, size);
        Page<OrderDTO> orderDTOPage = orderService.findList(openid, request);

        return ResultVOUtil.success(orderDTOPage.getContent());
    }

    /**
     *  3. order detail
     *  find the order details according to the openid and orderid
     */
    @GetMapping("/detail")
    public ResultVO<OrderDTO> detail (@RequestParam("openid") String openid,
                                      @RequestParam("orderId") String orderId){
        OrderDTO orderDTO = buyerService.findOrderOne(openid, orderId);
        return ResultVOUtil.success(orderDTO);
    }

    /**
     * 4. cancel an order
     */
    @PostMapping("/cancel")
    public ResultVO cancel (@RequestParam("openid") String openid,
                            @RequestParam("orderId") String orderId){
        buyerService.cancelOrder(openid, orderId);
        return ResultVOUtil.success();
    }

}
