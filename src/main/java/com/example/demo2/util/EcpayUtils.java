package com.example.demo2.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import java.util.TreeMap;

public class EcpayUtils {

	public static String generateCheckMacValue(String hashKey, String hashIv, Map<String, String> params) {
        TreeMap<String, String> sortedParams = new TreeMap<>(params);
        StringBuilder raw = new StringBuilder("HashKey=" + hashKey);
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            raw.append("&").append(entry.getKey()).append("=").append(entry.getValue());
        }
        raw.append("&HashIV=" + hashIv);

        String encoded = URLEncoder.encode(raw.toString(), StandardCharsets.UTF_8)
                .toLowerCase()
                .replace("%2d", "-").replace("%5f", "_").replace("%2e", ".")
                .replace("%2a", "*").replace("%28", "(").replace("%29", ")")
                .replace("%20", "+");

        return sha256(encoded).toUpperCase();
    }

    private static String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) { throw new RuntimeException(ex); }
    }
}
