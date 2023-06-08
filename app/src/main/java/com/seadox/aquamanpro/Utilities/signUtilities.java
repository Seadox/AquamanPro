package com.seadox.aquamanpro.Utilities;

import java.util.regex.Pattern;

public class signUtilities {
    public static final int PASSWORD_LEN = 6;
    public static final int NAME_LEN = 1;

    public static boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static boolean isPasswordValid(String password) {
        return password.length() >= PASSWORD_LEN;
    }

    public static boolean isNameValid(String name) {
        return name.length() >= NAME_LEN;
    }
}
