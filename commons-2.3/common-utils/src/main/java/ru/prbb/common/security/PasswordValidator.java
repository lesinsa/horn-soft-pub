package ru.prbb.common.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author LesinSA
 */
public final class PasswordValidator {

    public static final String PASSWD_CHARSET = "UTF-8";
    private final Pattern pattern;
    private final boolean plainPasswordEnabled;

    public PasswordValidator() {
        this(false);
    }

    public PasswordValidator(boolean plainPasswordEnabled) {
        pattern = Pattern.compile("(\\{.+\\})(.+)");
        this.plainPasswordEnabled = plainPasswordEnabled;
    }

    public boolean validate(String inputPassword, String encodedHash) {
        return validate(inputPassword, encodedHash, BinEncoding.BASE64);
    }

    public boolean validate(String inputPassword, String encodedHash, BinEncoding binEncoding) {

        try {
            // проверяем, хэш или пароль в открытом виде
            Matcher matcher = pattern.matcher(encodedHash);
            if (!matcher.matches()) {
                if (!plainPasswordEnabled) {
                    throw new IllegalArgumentException("Illegal pattern for encodedHash");
                }
                // comparing plain passwords
                return encodedHash.equals(inputPassword);
            }

            String alg1 = matcher.group(1);
            String alg = alg1.substring(1, alg1.length() - 1);
            byte[] expectedDigest = binEncoding.parse(matcher.group(2));
            byte[] actualDigest = getDigest(inputPassword, alg);
            return Arrays.equals(expectedDigest, actualDigest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    public String calculateEncodedDigest(String plainPassword, String alg) {
        return calculateEncodedDigest(plainPassword, alg, BinEncoding.BASE64);
    }

    public String calculateEncodedDigest(String plainPassword, String alg, BinEncoding binEncoding) {
        try {
            byte[] digest = getDigest(plainPassword, alg);
            String encodedDigest = binEncoding.print(digest);
            return String.format("{%s}%s", alg.toUpperCase(), encodedDigest);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private byte[] getDigest(String s, String alg) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest instance = MessageDigest.getInstance(alg);
        return instance.digest(s.getBytes(PASSWD_CHARSET));
    }
}
