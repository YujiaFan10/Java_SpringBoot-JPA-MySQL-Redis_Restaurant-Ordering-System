package com.example.order.controller;

import com.example.order.dataobject.ProductCategory;
import com.example.order.exception.OrderException;
import com.example.order.form.CategoryForm;
import com.example.order.service.CategoryService;
import com.example.order.utils.Key3Util;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/seller/category")
public class SellerCategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 1. list all categories
     * @param map
     * @return
     */
    @GetMapping("/list")
    public ModelAndView list(Map<String, Object> map){
        List<ProductCategory> productCategoryList = categoryService.findAll();
        map.put("categoryList", productCategoryList);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("category/list");
        mav.addObject(map);
        return mav;
    }

    /**
     * 2. enter the revise interface
     * @param categoryId
     * @param map
     * @return
     */
    @GetMapping("/revise")
    public ModelAndView revise(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                               Map<String, Object> map){
        if(categoryId != null){
            ProductCategory productCategory = categoryService.findOne(categoryId);
            map.put("productCategory", productCategory);
        }
        ModelAndView mav = new ModelAndView();
        mav.setViewName("category/revise");
        mav.addObject(map);
        return mav;
    }

    /**
     * 3. change category information & add a new category
     * @param categoryForm
     * @param map
     * @return
     */
    @PostMapping("/save")
    public ModelAndView save(@Valid CategoryForm categoryForm,
                             BindingResult bindingResult,
                             Map<String, Object> map){
        if(bindingResult.hasErrors()){
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url", "/order/seller/category/revise");
            ModelAndView mav = new ModelAndView();
            mav.setViewName("common/error");
            mav.addObject(map);
            return mav;
        }
        ProductCategory category = new ProductCategory();
        try{
            //if categoryForm is null, then create a new category
            if(categoryForm.getCategoryId() != null){
                category = categoryService.findOne(categoryForm.getCategoryId());
            }
            categoryForm.setCategoryId(category.getCategoryId());
            BeanUtils.copyProperties(categoryForm, category);
            categoryService.save(category);
        }catch (OrderException e){
            map.put("msg", e.getMessage());
            map.put("url", "/order/seller/category/revise");
            ModelAndView mav = new ModelAndView();
            mav.setViewName("common/error");
            mav.addObject(map);
            return mav;
        }
        map.put("url", "/order/seller/category/list");
        ModelAndView mav = new ModelAndView();
        mav.setViewName("common/success");
        mav.addObject(map);
        return mav;
    }


}
