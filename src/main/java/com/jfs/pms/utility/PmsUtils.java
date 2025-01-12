package com.jfs.pms.utility;

import java.security.SecureRandom;

import static com.jfs.pms.constants.Constants.ALPHA_NUMERICS;
import static com.jfs.pms.constants.Constants.NUMBERS;

public class PmsUtils {

    public static String generateUsername(String firstName, String lastName) {
        StringBuilder buffer = new StringBuilder(7);
        String numbers = NUMBERS;
        buffer.append(firstName.charAt(0));
        buffer.append(lastName.charAt(0));
        for (int i=0;i<5;i++) {
            int index = (int) (numbers.length() * Math.random());
            buffer.append(numbers.charAt(index));
        }
        return buffer.toString();
    }

    public static String generateProfileImage(String email) {
        String emailPrefix = "";
        if(email.contains("@")) {
            emailPrefix = email.split("@")[0];
        }
        return "https://robohash.org/" + emailPrefix;
    }

    public static String generatePassword() {
        StringBuilder password = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for(int i=0; i<10; ++i) {
            int index = random.nextInt(ALPHA_NUMERICS.length());
            password.append(ALPHA_NUMERICS.charAt(index));
        }
        return password.toString();
    }
}
