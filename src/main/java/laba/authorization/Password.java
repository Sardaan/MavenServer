package laba.authorization;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Random;

public class Password {

    public static String generatePassword(String str, String salt) {
        return encryptPassword(str, salt);
    }

    public static String generateString(int length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        return generatedString;
    }

    public static String encryptPassword(String password, String salt) {
        String sha = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-384");
            sha = byteToHex(crypt.digest((password+salt).getBytes()));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("There's no algorithm to encrypt");
        }
        return sha;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

}

