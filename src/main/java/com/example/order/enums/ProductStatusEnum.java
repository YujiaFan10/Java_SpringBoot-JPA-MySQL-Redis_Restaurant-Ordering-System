package com.example.order.enums;

import lombok.Getter;

/**
 * the status of product: available, unavailable
 */

@Getter
public enum ProductStatusEnum implements CodeEnum{

    UP(0, "available"),
    DOWN(1, "unavailable")
    ;

    private Integer code;

    private String message;

    ProductStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
