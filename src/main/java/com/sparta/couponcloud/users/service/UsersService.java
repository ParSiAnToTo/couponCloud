package com.sparta.couponcloud.users.service;

import com.sparta.couponcloud.users.entity.Role;
import com.sparta.couponcloud.users.entity.Users;
import com.sparta.couponcloud.users.repository.UsersRepository;
import com.sparta.couponcloud.users.request.LoginRequestDto;
import com.sparta.couponcloud.users.request.SignupRequestDto;
import com.sparta.couponcloud.users.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersService {

    private final UsersRepository usersRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    public void signup(SignupRequestDto signupRequestDto) {
        Users newUser = new Users(
                signupRequestDto.getEmail(),
                signupRequestDto.getPassword(),
                signupRequestDto.getPhoneNumber(),
                signupRequestDto.getAddress(),
                signupRequestDto.getName(),
                Role.USER);

        usersRepository.save(newUser);
    }

    public String login(LoginRequestDto loginRequestDto) {
        Users users = usersRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("잘못된 이메일 또는 비밀번호입니다."));

        if (!users.getPassword().equals(loginRequestDto.getPassword())) {
            throw new IllegalArgumentException("잘못된 이메일 또는 비밀번호입니다.");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(users.getUserId(), users.getRole());

        String refreshToken = jwtTokenProvider.generateRefreshToken(users.getUserId(), users.getRole());

        String redisKey = "refreshToken:" + users.getUserId();
        redisTemplate.opsForValue().set(
                redisKey,
                refreshToken,
                jwtTokenProvider.getRefreshTokenExpiration() / 1000,
                TimeUnit.SECONDS
        );

        return accessToken;
    }

    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();
        String redisKey = "refreshToken:" + userId;
        redisTemplate.delete(redisKey);
        log.info("Removed refresh token for userId: {}", userId);
    }
}
