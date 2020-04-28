package com.example.order.utils;

import java.util.Random;

public class Key3Util {

    /** to get unique primary key: time + random number, within 6 digits */
    public static synchronized String genUniqueKey(){

        Random random = new Random();

        Integer number = random.nextInt(90) + 10;

        return String.valueOf(number);
    }

}
