package com.example.order.service;

import com.example.order.VO.ResponseVO;
import com.example.order.dataobject.SellerInfo;

public interface SellerService {

    SellerInfo findSellerInfoByUsername(String username);

    /**
     * Registration
     */
    ResponseVO<SellerInfo> register(SellerInfo sellerInfo);

    /**
     * Login
     */
    ResponseVO<SellerInfo> login(String username, String password);
}
