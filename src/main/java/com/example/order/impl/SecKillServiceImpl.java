package com.example.order.impl;

import com.example.order.enums.ResultEnum;
import com.example.order.exception.OrderException;
import com.example.order.service.RedisLock;
import com.example.order.service.SecKillService;
import com.example.order.utils.KeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SecKillServiceImpl implements SecKillService {

    @Autowired
    private RedisLock redisLock;

    private static final int TIMEOUT = 10 * 1000; //10 seconds

    /**
     * Create product and its stock for flash sale
     * orders: # total users have ordered the product, check if it equals to stock
     */
    static Map<String, Integer> products;
    static Map<String, Integer> stock;
    static Map<String, String> orders;
    static {
        products = new HashMap<>();
        stock = new HashMap<>();
        orders = new HashMap<>();
        products.put("12345", 10000); //productId + stock
        stock.put("12345", 10000);
    }

    private String queryMap(String productId){
        return "Now the product: " + productId + " is on sale. "
                + "remaining "+stock.get(productId)
                + ". Number of successful orders is "+ orders.size() + ".";
    }

    @Override
    public String querySecKillProductInfo(String productId) {
        return this.queryMap(productId);
    }

    @Override
    public void orderProductMockDiffUser(String productId) {

        /** add redis lock */
        long time = System.currentTimeMillis() + TIMEOUT;
        if(redisLock.lock(productId, String.valueOf(time))){
            throw new OrderException(ResultEnum.BUSY_NETWORK);
        }

        // 1. check the stock, if 0, the sale is over
        int stockNum = stock.get(productId);
        if(stockNum == 0){
            throw new OrderException(ResultEnum.STOCK_ZERO);
        } else {
            // 2. check out
            orders.put(KeyUtil.genUniqueKey(), productId);
            // 3. reduce stock
            stockNum = stockNum - 1;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            stock.put(productId, stockNum);
        }

        /** redis unlock **/
        redisLock.unlock(productId, String.valueOf(time));
    }
}
