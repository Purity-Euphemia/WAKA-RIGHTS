package com.WakaRights.utils;

import java.util.UUID;

public class FileEncryptionUtil {
    public static String save(String base64) {
        return "/secure/" + UUID.randomUUID();
    }
}