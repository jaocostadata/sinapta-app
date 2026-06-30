package br.com.sinapta.ecossistema.common.crypto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES-256-GCM encryption used for sensitive fields (passwords, API tokens)
 * stored in the database. The key is derived from app.crypto.secret-key so the
 * raw value never needs to be exactly 32 bytes long.
 */
@Service
public class CryptoService {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int IV_LENGTH_BYTES = 12;
    private static final int TAG_LENGTH_BITS = 128;

    private final SecretKeySpec key;

    public CryptoService(@Value("${app.crypto.secret-key}") String secretKey) {
        this.key = deriveKey(secretKey);
    }

    public String encrypt(String plainText) {
        if (plainText == null) {
            return null;
        }
        try {
            byte[] iv = new byte[IV_LENGTH_BYTES];
            new SecureRandom().nextBytes(iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH_BITS, iv));
            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            byte[] payload = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, payload, 0, iv.length);
            System.arraycopy(cipherText, 0, payload, iv.length, cipherText.length);
            return Base64.getEncoder().encodeToString(payload);
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao criptografar valor", e);
        }
    }

    public String decrypt(String storedValue) {
        if (storedValue == null) {
            return null;
        }
        try {
            byte[] payload = Base64.getDecoder().decode(storedValue);
            byte[] iv = new byte[IV_LENGTH_BYTES];
            System.arraycopy(payload, 0, iv, 0, IV_LENGTH_BYTES);
            byte[] cipherText = new byte[payload.length - IV_LENGTH_BYTES];
            System.arraycopy(payload, IV_LENGTH_BYTES, cipherText, 0, cipherText.length);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH_BITS, iv));
            return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao descriptografar valor", e);
        }
    }

    private SecretKeySpec deriveKey(String secretKey) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] hashed = sha256.digest(secretKey.getBytes(StandardCharsets.UTF_8));
            return new SecretKeySpec(hashed, "AES");
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao derivar chave de criptografia", e);
        }
    }
}
