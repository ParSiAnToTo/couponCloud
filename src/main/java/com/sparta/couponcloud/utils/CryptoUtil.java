package com.sparta.couponcloud.utils;

import com.sparta.couponcloud.config.CryptoConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.SecureRandom;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class CryptoUtil {

    private final CryptoConfig cryptoConfig;

    public String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(cryptoConfig.isIvEnabled() ?
                cryptoConfig.getAlgorithm() + "/GCM/NoPadding" :
                cryptoConfig.getAlgorithm());

        SecretKey secretKey = cryptoConfig.getSecretKey();

        if (cryptoConfig.isIvEnabled()) {
            byte[] iv = new byte[12];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

            byte[] encryptedData = cipher.doFinal(data.getBytes());
            byte[] encryptedWithIv = new byte[iv.length + encryptedData.length];
            System.arraycopy(iv, 0, encryptedWithIv, 0, iv.length);
            System.arraycopy(encryptedData, 0, encryptedWithIv, iv.length, encryptedData.length);

            return Base64.getEncoder().encodeToString(encryptedWithIv);
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
        }
    }

    public String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance(cryptoConfig.isIvEnabled() ?
                cryptoConfig.getAlgorithm() + "/GCM/NoPadding" :
                cryptoConfig.getAlgorithm());

        SecretKey secretKey = cryptoConfig.getSecretKey();

        byte[] decodedData = Base64.getDecoder().decode(encryptedData);

        if (cryptoConfig.isIvEnabled()) {
            byte[] iv = new byte[12];
            System.arraycopy(decodedData, 0, iv, 0, iv.length);

            GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

            byte[] originalData = cipher.doFinal(decodedData, iv.length, decodedData.length - iv.length);
            return new String(originalData);
        } else {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(decodedData));
        }
    }

}
