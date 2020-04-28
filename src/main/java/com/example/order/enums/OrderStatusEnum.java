package com.example.order.enums;

import lombok.Getter;

@Getter
public enum OrderStatusEnum implements CodeEnum{

    NEW(0, "order placed"),
    FINISHED(1, "order finished"),
    CANCEL(2, "order canceled"),
    ;

    private Integer code;

    private String message;

    OrderStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
