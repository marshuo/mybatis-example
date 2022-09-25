package com.marshuo.utils;

import java.util.UUID;

/**
 * @author mars
 * @date 2022/09/24
 */
public class UUIDUtils {
    public static String getId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    
    public static void main(String[] args) {
        String id = UUIDUtils.getId();
        System.out.println("id = " + id);
    }
}
