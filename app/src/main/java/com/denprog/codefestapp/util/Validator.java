package com.denprog.codefestapp.util;

import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private static final Pattern pattern = Pattern.compile("^[A-Z][a-z]+(?:[-' ][A-Z]+)*$");

    public static boolean areInputNull(String ...inputs) {
        for (String input : inputs) {
            if (input == null || input.isEmpty() || input.isBlank()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmailValid (String email) {
        Matcher matcher = Patterns.EMAIL_ADDRESS.matcher(email);
        return matcher.matches();
    }

    public static boolean isNameValid(String name) {
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

}
