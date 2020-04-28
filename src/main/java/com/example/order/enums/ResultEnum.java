package com.example.order.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {

    SUCCESS(0, "success"),
    PRODUCT_NOT_EXIST(10, "product not exists"),
    PRODUCT_STOCK_NOT_ENOUGH(11, "not enough stock"),
    ORDER_NOT_EXIST(12, "order not exists"),
    ORDER_DETAIL_NOT_EXIST(13, "order detail not exists"),
    ORDER_STATUS_ERROR(14,"order status is not right"),
    ORDER_UPDATE_FAIL(15,"order update fail"),
    ORDER_DETAIL_LIST_EMPTY(16, "no order detail in the order"),
    ORDER_PAYMENT_STATUS_ERROR(17, "payment status is not right"),
    CART_EMPTY(18, "shopping cart is empty"),
    ORDER_OWNER_ERROR(19, "the order is not owned by the user"),
    ORDER_CANCEL_SUCCESS(20, "order cancelled successfully!"),
    ORDER_FINISH_SUCCESS(21, "order finished successfully!"),
    PRODUCT_ALREADY_ON_SHELVES(22,"product is already put on shelves"),
    PRODUCT_ALREADY_OFF_SHELVES(23,"product is already put off shelves"),
    PRODUCT_PUT_ON_SHELVES(24, "product put on shelves successfully!"),
    PRODUCT_PUT_OFF_SHELVES(25, "product put off shelves successfully!"),
    WECHAT_MP_ERROR(26, "wechat mp service error"),

    ERROR(-1,"Server Error"),
    USERNAME_EXIST(27, "username already exists"),
    USERNAME_OR_PASSWORD_ERROR(28,"wrong username or password "),
    LOGIN_FAIL(29,"login fails, wrong login information"),
    LOGOUT_SUCCESS(30, "logout successfully"),

    //to test concurrency
    STOCK_ZERO(31, "No items available"),
    BUSY_NETWORK(32, " Line Busy! Please try again! ^_^ "),

    PARAM_ERROR(1, "wrong parameter"),

    ;

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
