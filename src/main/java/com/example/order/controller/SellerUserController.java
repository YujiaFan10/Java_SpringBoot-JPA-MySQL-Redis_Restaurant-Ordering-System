package com.example.order.controller;

import com.example.order.VO.ResponseVO;
import com.example.order.config.ProjectUrlConfig;
import com.example.order.constant.CookieConstant;
import com.example.order.constant.RedisConstant;
import com.example.order.dataobject.SellerInfo;
import com.example.order.enums.ResultEnum;
import com.example.order.service.SellerService;
import com.example.order.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/seller/user")
@Slf4j
public class SellerUserController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    @PostMapping("/register")
    public ResponseVO<SellerInfo> register(@Valid @RequestBody SellerInfo sellerInfo) {
        return sellerService.register(sellerInfo);
    }

    @GetMapping("/start")
    public  ModelAndView start(Map<String, Object> map){
        String username = new String();
        String password = new String();
        map.put("username", username);
        map.put("password", password);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("user/start");
        mav.addObject(map);
        return mav;
    }

    /**
    @PostMapping("/login")
    public ResponseVO<SellerInfo> login(@Valid @RequestBody SellerInfo sellerInfo) {
        return sellerService.register(sellerInfo);
    }
    */

    @GetMapping("/login")
    public ModelAndView login(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                                HttpServletResponse response,
                                Map<String, Object> map){
        // 1. find the username in the database
        SellerInfo sellerInfo = sellerService.findSellerInfoByUsername(username);
        //String rightPassword = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8))
        if(sellerInfo == null || !password.equals(sellerInfo.getPassword())){
            map.put("msg", ResultEnum.LOGIN_FAIL.getMessage());
            map.put("url", "/order/seller/order/list");
            ModelAndView mav = new ModelAndView();
            mav.setViewName("common/error");
            mav.addObject(map);
            return mav;
        }
        //2. set token to redis
        String token = UUID.randomUUID().toString();
        Integer expire = RedisConstant.EXPIRE;

        stringRedisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX, token), username, expire, TimeUnit.SECONDS);

        //3. set token to cookie
        CookieUtil.set(response, CookieConstant.TOKEN, token, expire);

        return new ModelAndView("redirect:" + projectUrlConfig.getOrder() + "/seller/order/list");
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request,
                               HttpServletResponse response,
                               Map<String, Object> map){
        // 1. find token in cookie
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        //2. remove token from redis
        stringRedisTemplate.opsForValue().getOperations().delete(String.format(RedisConstant.TOKEN_PREFIX, cookie.getValue()));
        //3. remove token from cookie: set expire time as 0s
        CookieUtil.set(response, CookieConstant.TOKEN, null, 0);

        map.put("msg", ResultEnum.LOGOUT_SUCCESS.getMessage());
        map.put("url", "/order/seller/product/list");
        ModelAndView mav = new ModelAndView();
        mav.setViewName("common/success");
        mav.addObject(map);
        return mav;
    }

}
