package com.sparta.couponcloud.users.controller;

import com.sparta.couponcloud.users.request.*;
import com.sparta.couponcloud.users.response.MyDataResponseDto;
import com.sparta.couponcloud.users.service.EmailService;
import com.sparta.couponcloud.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;
    private final EmailService emailService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto signupRequestDto) {
        usersService.signup(signupRequestDto);
        return ResponseEntity.ok("Signup successful");
    }

    @PostMapping("/email/send")
    public ResponseEntity<String> sendVerificationEmail(@RequestBody EmailSendRequestDto emailSendRequestDto) {
        emailService.sendVerificationEmail(emailSendRequestDto.getEmail());
        return ResponseEntity.ok("Verification email sent successfully.");
    }

    @PostMapping("/email/verify")
    public ResponseEntity<Boolean> verifyEmail(@RequestBody EmailVerificationRequestDto verificationRequestDto) {
        boolean isVerified = emailService.verifyEmail(verificationRequestDto.getEmail(), verificationRequestDto.getVerificationCode());
        return ResponseEntity.ok(isVerified);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto) {
        String accessToken = usersService.login(loginRequestDto);
        return ResponseEntity.ok(accessToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        usersService.logout();
        return ResponseEntity.ok("Logout successful");
    }

    @GetMapping("/my-data")
    public ResponseEntity<MyDataResponseDto> getMyData() {
        MyDataResponseDto myDataResponseDto = usersService.getMyData();
        return ResponseEntity.ok(myDataResponseDto);
    }

    @PostMapping("/my-data")
    public ResponseEntity<String> updateMyData(@RequestBody DataUpdateRequestDto updateRequestDto) {
        usersService.updateMyData(updateRequestDto);
        return ResponseEntity.ok("User data updated successfully");
    }

    @PostMapping("/password")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordUpdateRequestDto pwUpdateDto) {
        usersService.updatePassword(pwUpdateDto);
        return ResponseEntity.ok("Password updated successfully");
    }

}
