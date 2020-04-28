package com.example.order.controller;

import com.example.order.dto.OrderDTO;
import com.example.order.enums.ResultEnum;
import com.example.order.exception.OrderException;
import com.example.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("/seller/order")
@Slf4j
public class SellerOrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 1. get all order lists
     */
    @GetMapping("/list")
    public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "size", defaultValue = "8") Integer size,
                             Map<String, Object> map){
        PageRequest request = PageRequest.of(page-1, size);
        Page<OrderDTO> orderDTOPage = orderService.findList(request);
        map.put("orderDTOPage", orderDTOPage);
        map.put("currentPage", page);
        map.put("size", size);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("order/list");
        mav.addObject(map);
        return mav;
    }

    /**
     *  2. cancel an order
     */
    @GetMapping("/cancel")
    public ModelAndView cancel(@RequestParam("orderId") String orderId,
                               Map<String, Object> map){
        try {
            OrderDTO orderDTO = orderService.findOne(orderId);
            orderService.cancel(orderDTO);
        } catch (OrderException e){
            log.error("[seller cancels an order] something wrong {}", e);
            map.put("msg", ResultEnum.ORDER_NOT_EXIST.getMessage());
            map.put("url", "/order/seller/order/list");
            ModelAndView mav = new ModelAndView();
            mav.setViewName("common/error");
            mav.addObject(map);
            return mav;
        }

        map.put("msg", ResultEnum.ORDER_CANCEL_SUCCESS.getMessage());
        map.put("url", "/order/seller/order/list");
        ModelAndView mav = new ModelAndView();
        mav.setViewName("common/success");
        mav.addObject(map);
        return mav;

    }

    /**
     * 3. check order details
     * @param orderId
     * @param map
     * @return
     */
    @GetMapping("/details")
    public ModelAndView details(@RequestParam("orderId") String orderId,
                                Map<String, Object> map) {
        OrderDTO orderDTO = new OrderDTO();
        try {
            orderDTO = orderService.findOne(orderId);
        } catch (OrderException e) {
            log.error("[check order details] something wrong {}", e);
            map.put("msg", e.getMessage());
            map.put("url", "/order/seller/order/list");
            ModelAndView mav = new ModelAndView();
            mav.setViewName("common/error");
            mav.addObject(map);
            return mav;
        }

        map.put("orderDTO", orderDTO);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("order/details");
        mav.addObject(map);
        return mav;
    }

    /** 4. finish an order
     * @param orderId
     * @param map
     * @return
     */
    @GetMapping("/finish")
    public ModelAndView finish(@RequestParam("orderId") String orderId,
                               Map<String, Object> map) {
        try {
            OrderDTO orderDTO = orderService.findOne(orderId);
            orderService.finish(orderDTO);
        } catch (OrderException e){
            log.error("[seller finishes an order] something wrong {}", e);
            map.put("msg", e.getMessage());
            map.put("url", "/order/seller/order/list");
            ModelAndView mav = new ModelAndView();
            mav.setViewName("common/error");
            mav.addObject(map);
            return mav;
        }

        map.put("msg", ResultEnum.ORDER_FINISH_SUCCESS.getMessage());
        map.put("url", "/order/seller/order/list");
        ModelAndView mav = new ModelAndView();
        mav.setViewName("common/success");
        mav.addObject(map);
        return mav;

    }

}
