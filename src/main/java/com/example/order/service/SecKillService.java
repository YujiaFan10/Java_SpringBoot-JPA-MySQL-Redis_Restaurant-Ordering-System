package com.example.order.service;

public interface SecKillService {

    /**
     * check the product information for flash sale
     * @param productId
     * @return
     */
    String querySecKillProductInfo(String productId);

    /**
     * Simulates requests from different users for the same product
     * @param productId
     */
    void orderProductMockDiffUser(String productId);
}
