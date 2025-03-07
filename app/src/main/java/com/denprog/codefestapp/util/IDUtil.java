package com.denprog.codefestapp.util;

import java.util.Random;

public class IDUtil {
    public static String generateRandomCharacters(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            result.append(chars.charAt(index));
        }

        return result.toString();
    }
}
