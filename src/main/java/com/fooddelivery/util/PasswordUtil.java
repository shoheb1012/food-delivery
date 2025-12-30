package com.fooddelivery.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {
    private static final BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    public static String hash(String rawPassword){
        return passwordEncoder.encode(rawPassword);
    }

    public static boolean matches(String raw,String hashed){
        return passwordEncoder.matches(raw,hashed);
    }
}
