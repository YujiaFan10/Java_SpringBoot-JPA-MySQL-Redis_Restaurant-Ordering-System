package com.example.order.impl;

import com.example.order.VO.ResponseVO;
import com.example.order.dataobject.SellerInfo;
import com.example.order.enums.ResultEnum;
import com.example.order.repository.SellerInfoRepository;
import com.example.order.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerInfoRepository repository;

    @Override
    public SellerInfo findSellerInfoByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public ResponseVO<SellerInfo> register(SellerInfo sellerInfo) {
        sellerInfo.setPassword(DigestUtils.md5DigestAsHex(
                sellerInfo.getPassword().getBytes(StandardCharsets.UTF_8)));
        SellerInfo result = repository.save(sellerInfo);
        if (result == null) {
            return ResponseVO.error(ResultEnum.ERROR);
        }
        return ResponseVO.success(result);
    }

    @Override
    public ResponseVO<SellerInfo> login(String username, String password) {
        return null;
    }
}
