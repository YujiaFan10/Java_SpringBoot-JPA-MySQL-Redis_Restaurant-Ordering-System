package com.example.order.utils;

import java.util.Random;

public class KeyUtil {

    /** to get unique primary key: time + random number, within 6 digits */
    public static synchronized String genUniqueKey(){

        Random random = new Random();

        Integer number = random.nextInt(900000) + 100000;

        return System.currentTimeMillis() + String.valueOf(number);
    }

}
