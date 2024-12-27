package com.sparta.couponcloud.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Getter
@Configuration
public class CryptoConfig {

    @Value("${crypto.algorithm}")
    private String algorithm;

    @Value("${crypto.secret.key}")
    private String secretKey;

    @Value("${crypto.key.size}")
    private int keySize;

    @Value("${crypto.iv.enabled}")
    private boolean ivEnabled;

    public SecretKey getSecretKey() {
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        return new SecretKeySpec(decodedKey, algorithm);
    }
}
