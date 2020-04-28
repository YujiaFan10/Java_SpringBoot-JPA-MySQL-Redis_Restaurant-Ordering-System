package com.example.order.controller;

import com.example.order.dataobject.ProductCategory;
import com.example.order.dataobject.ProductInfo;
import com.example.order.dto.OrderDTO;
import com.example.order.enums.ResultEnum;
import com.example.order.exception.OrderException;
import com.example.order.form.ProductForm;
import com.example.order.service.CategoryService;
import com.example.order.service.ProductService;
import com.example.order.utils.Key3Util;
import com.example.order.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/seller/product")
@Slf4j
public class SellerProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 1. get all products
     */
    @GetMapping("/list")
    public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "size", defaultValue = "5") Integer size,
                             Map<String, Object> map){
        PageRequest request = PageRequest.of(page-1, size);
        Page<ProductInfo> productInfoPage = productService.findAll(request);
        map.put("productInfoPage", productInfoPage);
        map.put("currentPage", page);
        map.put("size", size);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("product/list");
        mav.addObject(map);
        return mav;
    }

    /**
     * 2. put product on shelves
     * @param productId
     * @param map
     * @return
     */
    @GetMapping("/putOn")
    public ModelAndView putOn(@RequestParam("productId") String productId,
                              Map<String, Object> map){
        try{
            ProductInfo productInfo= productService.putOnShelves(productId);
        }catch (OrderException e){
            map.put("msg", e.getMessage());
            map.put("url", "/order/seller/product/list");
            ModelAndView mav = new ModelAndView();
            mav.setViewName("common/error");
            mav.addObject(map);
            return mav;
        }
        map.put("msg", ResultEnum.PRODUCT_PUT_ON_SHELVES.getMessage());
        map.put("url","/order/seller/product/list");
        ModelAndView mav = new ModelAndView();
        mav.setViewName("common/success");
        mav.addObject(map);
        return mav;
    }

    /**
     * 3. put product off shelves
     * @param productId
     * @param map
     * @return
     */
    @GetMapping("/putOff")
    public ModelAndView putOff(@RequestParam("productId") String productId,
                              Map<String, Object> map){
        try{
            ProductInfo productInfo= productService.putOffShelves(productId);
        }catch (OrderException e){
            map.put("msg", e.getMessage());
            map.put("url", "/order/seller/product/list");
            ModelAndView mav = new ModelAndView();
            mav.setViewName("common/error");
            mav.addObject(map);
            return mav;
        }
        map.put("msg", ResultEnum.PRODUCT_PUT_OFF_SHELVES.getMessage());
        map.put("url","/order/seller/product/list");
        ModelAndView mav = new ModelAndView();
        mav.setViewName("common/success");
        mav.addObject(map);
        return mav;
    }

    /**
     * 4. enter the revise interface
     * @param productId
     * @param map
     * @return
     */
    @GetMapping("/revise")
    @CacheEvict(cacheNames = "product", key = "123")
    public ModelAndView revise(@RequestParam(value = "productId", required = false) String productId,
                               Map<String, Object> map){
        if(!StringUtils.isEmpty(productId)){
            ProductInfo productInfo = productService.findOne(productId);
            map.put("productInfo", productInfo);
        }
        //search all categories
        List<ProductCategory> categoryList = categoryService.findAll();
        map.put("categoryList", categoryList);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("product/revise");
        mav.addObject(map);
        return mav;
    }

    /**
     *  5. change product information & add a new product
     * @param productForm
     * @param bindingResult
     * @param map
     * @return
     */
    @PostMapping("/save")
    public ModelAndView save(@Valid ProductForm productForm,
                             BindingResult bindingResult,
                             Map<String, Object> map){
        if(bindingResult.hasErrors()){
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url", "/order/seller/product/revise");
            ModelAndView mav = new ModelAndView();
            mav.setViewName("common/error");
            mav.addObject(map);
            return mav;
        }
        ProductInfo productInfo = new ProductInfo();
        try{
            //if productId is null, then create a new productInfo
            if(!StringUtils.isEmpty(productForm.getProductId())){
                productInfo = productService.findOne(productForm.getProductId());
            }
            else{
                productForm.setProductId(Key3Util.genUniqueKey());
            }
            BeanUtils.copyProperties(productForm, productInfo);
            productService.save(productInfo);
        }catch(OrderException e){
            map.put("msg", e.getMessage());
            map.put("url", "/order/seller/product/revise");
            ModelAndView mav = new ModelAndView();
            mav.setViewName("common/error");
            mav.addObject(map);
            return mav;
        }
        map.put("url", "/order/seller/product/list");
        ModelAndView mav = new ModelAndView();
        mav.setViewName("common/success");
        mav.addObject(map);
        return mav;
    }

}
