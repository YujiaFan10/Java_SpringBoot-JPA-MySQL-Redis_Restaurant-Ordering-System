package com.example.order.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class OrderForm {

    /** Buyer Name */
    @NotEmpty(message = "Name Required")
    private String name;

    /** Buyer Phone Number */
    @NotEmpty(message = "Phone Required")
    private String phone;

    /** Buyer Address */
    @NotEmpty(message = "Address Required")
    private String address;

    /** Buyer Openid */
    @NotEmpty(message = "Openid Required")
    private String openid;

    /** Shopping Cart */
    @NotEmpty(message = "Shopping cart cannot be empty")
    private String items;

}
