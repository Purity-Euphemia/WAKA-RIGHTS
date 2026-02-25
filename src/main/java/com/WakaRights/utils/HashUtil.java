package com.WakaRights.utils;

import java.security.MessageDigest;
import java.util.Base64;

public class HashUtil {
    public static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return Base64.getEncoder().encodeToString(md.digest(input.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Hash error");
        }
    }
}