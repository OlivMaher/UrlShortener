package com.olivmaher.urlshortener.util;

public class Base62Encoder {

    private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String encode(long id){
        id = id + 100000;
        StringBuilder sb = new StringBuilder();
        while(id > 0){
            int index = (int) id % 62;
            sb.append(CHARS.charAt(index));
            id /= 62;
        }
        return sb.reverse().toString();
    }
}
