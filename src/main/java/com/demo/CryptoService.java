package com.demo;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

public class CryptoService {

    private Cipher cipher;
    private AlgorithmParameterSpec ivSpec;
    private Key keySpec;
    private static final String				ALGORITHM			= "AES";
    private static final String				CIPHER_DESCRIPTOR	= "AES/CBC/PKCS5Padding";
    private final static byte[] IV = { 17, 15, 9, 5, 13, 3, 18, 12, 18, 2, 19, 20, 6, 18, 1, 5 };
    private final static String SECRET_KEY = "PBKDF2WithHmSHA1";

    public CryptoService() {
        ivSpec = new IvParameterSpec(IV);
        keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        try {
            cipher = Cipher.getInstance(CIPHER_DESCRIPTOR);
        } catch (Exception ex) {
            throw new RuntimeException("Unable to continue; can't get an instance of cipher.", ex);
        }
    }

    String encrypt(String data) throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        final byte[] encryptedVal = cipher.doFinal(data.getBytes());
        return DatatypeConverter.printBase64Binary(encryptedVal);
    }

    public String decrypt(String data) throws Exception {

        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        final byte[] decodedValue = DatatypeConverter.parseBase64Binary(data);
        final byte[] decryptedValue = cipher.doFinal(decodedValue);

        return new String(decryptedValue);
    }
}
