package com.example.order.dto;

import lombok.Data;

/** shopping cart
 * offered by the url
 */

@Data
public class CartDTO {

    private String productId;

    private Integer productQuantity;

    public CartDTO(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
