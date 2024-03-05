package com.adamszablewski.SocialMediaApp.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

@Component
public class EncryptionUtil {

    private static String secretKey;
    @Autowired
    public EncryptionUtil(@Value("${message-encryption.key}") String value) {
        EncryptionUtil.secretKey = value;
        MY_KEY = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
    }
    private static final String ALGORITHM = "AES";
    private static Key MY_KEY;

    private static Cipher getCipher(int mode) throws Exception {
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(mode, MY_KEY);
        return c;
    }

    public static String encryptText(String valueToEnc) throws Exception {
        return Base64.getEncoder().encodeToString(getCipher(Cipher.ENCRYPT_MODE).doFinal(valueToEnc.getBytes()));
    }

    public static String decryptText(String encryptedValue) throws Exception {
        return new String(getCipher(Cipher.DECRYPT_MODE).doFinal(Base64.getDecoder().decode(encryptedValue)));
    }
}
