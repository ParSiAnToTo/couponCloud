package com.sparta.couponcloud.users.service;

import com.sparta.couponcloud.users.entity.Role;
import com.sparta.couponcloud.users.entity.Users;
import com.sparta.couponcloud.users.repository.UsersRepository;
import com.sparta.couponcloud.users.request.DataUpdateRequestDto;
import com.sparta.couponcloud.users.request.LoginRequestDto;
import com.sparta.couponcloud.users.request.PasswordUpdateRequestDto;
import com.sparta.couponcloud.users.request.SignupRequestDto;
import com.sparta.couponcloud.users.response.MyDataResponseDto;
import com.sparta.couponcloud.users.security.JwtTokenProvider;
import com.sparta.couponcloud.utils.CryptoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersService {

    private final UsersRepository usersRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CryptoUtil cryptoUtil;

    public void signup(SignupRequestDto signupRequestDto) {
        if (!signupRequestDto.isEmailVerified()) {
            throw new IllegalArgumentException("Email is not verified.");
        }

        try {
            Users newUser = new Users(
                    signupRequestDto.getEmail(),
                    passwordEncoder.encode(signupRequestDto.getPassword()),
                    cryptoUtil.encrypt(signupRequestDto.getPhoneNumber()),
                    cryptoUtil.encrypt(signupRequestDto.getAddress()),
                    cryptoUtil.encrypt(signupRequestDto.getName()),
                    Role.USER);

            usersRepository.save(newUser);
        } catch (Exception e) {
            log.error("Error during user signup: {}", e.getMessage());
            throw new RuntimeException("회원가입 중 문제가 발생했습니다.");
        }
    }

    public String login(LoginRequestDto loginRequestDto) {
        Users users = usersRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("잘못된 이메일 또는 비밀번호입니다."));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), users.getPassword())) {
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
        Long userId = getAuthenticatedUserId();
        String redisKey = "refreshToken:" + userId;
        redisTemplate.delete(redisKey);
        log.info("Removed refresh token for userId: {}", userId);
    }

    public MyDataResponseDto getMyData() {
        Long userId = getAuthenticatedUserId();
        Users users = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        try {
            return new MyDataResponseDto(
                    users.getEmail(),
                    cryptoUtil.decrypt(users.getPhoneNumber()),
                    cryptoUtil.decrypt(users.getAddress()),
                    cryptoUtil.decrypt(users.getName())
            );
        } catch (Exception e) {
            log.error("Error decrypting user data for user ID: {}", userId, e);
            throw new RuntimeException("사용자 데이터를 복호화하는 중 문제가 발생했습니다.");
        }
    }

    public void updateMyData(DataUpdateRequestDto updateRequestDto){
        Long userId = getAuthenticatedUserId();
        Users users = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        try {
            String encryptedPhoneNumber = updateRequestDto.getPhoneNumber() != null ?
                    cryptoUtil.encrypt(updateRequestDto.getPhoneNumber()) : users.getPhoneNumber();
            String encryptedAddress = updateRequestDto.getAddress() != null ?
                    cryptoUtil.encrypt(updateRequestDto.getAddress()) : users.getAddress();
            String encryptedName = updateRequestDto.getName() != null ?
                    cryptoUtil.encrypt(updateRequestDto.getName()) : users.getName();

            users.updateProfile(encryptedPhoneNumber, encryptedAddress, encryptedName);
            usersRepository.save(users);

        } catch (Exception e) {
            log.error("Error updating user data for user ID: {}", userId, e);
            throw new RuntimeException("사용자 데이터를 업데이트하는 중 문제가 발생했습니다.");
        }
    }


    public void updatePassword(PasswordUpdateRequestDto pwUpdateDto) {
        Long userId = getAuthenticatedUserId();
        Users users = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(pwUpdateDto.getCurrentPassword(), users.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        users.changePassword(passwordEncoder.encode(pwUpdateDto.getNewPassword()));
        usersRepository.save(users);
    }

    public Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }
        return (Long) authentication.getPrincipal();
    }
}
