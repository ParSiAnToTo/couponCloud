package com.sparta.couponcloud.users.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final EmailSender emailSender;
    private final RedisTemplate<String, String> redisTemplate;

    private static final long EMAIL_VERIFICATION_EXPIRATION = 5 * 60;

    public void sendVerificationEmail(String email) {
        String verificationCode = generateVerificationCode();
        redisTemplate.opsForValue().set("email:verify:" + email, verificationCode, EMAIL_VERIFICATION_EXPIRATION, TimeUnit.SECONDS);

        emailSender.sendEmail(
                email,
                "Email Verification",
                "Your verification code is: " + verificationCode
        );
        log.info("Verification code sent to email: {}", email);
    }

    public boolean verifyEmail(String email, String verificationCode) {
        String storedCode = redisTemplate.opsForValue().get("email:verify:" + email);
        if (storedCode == null || !storedCode.equals(verificationCode)) {
            return false;
        }

        redisTemplate.delete("email:verify:" + email);
        return true;
    }

    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }
}

