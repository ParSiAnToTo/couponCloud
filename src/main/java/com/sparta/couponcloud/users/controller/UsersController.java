package com.sparta.couponcloud.users.controller;

import com.sparta.couponcloud.users.request.LoginRequestDto;
import com.sparta.couponcloud.users.request.SignupRequestDto;
import com.sparta.couponcloud.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto signupRequestDto) {
        usersService.signup(signupRequestDto);
        return ResponseEntity.ok("Signup successful");
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
}
