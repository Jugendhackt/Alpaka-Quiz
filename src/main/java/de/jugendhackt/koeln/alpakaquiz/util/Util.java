package de.jugendhackt.koeln.alpakaquiz.util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public final class Util {
    public static final String RANDOM_STRING_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final Random random = ThreadLocalRandom.current();

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(RANDOM_STRING_CHARS.charAt(random.nextInt(RANDOM_STRING_CHARS.length())));
        }
        return sb.toString();
    }
}
