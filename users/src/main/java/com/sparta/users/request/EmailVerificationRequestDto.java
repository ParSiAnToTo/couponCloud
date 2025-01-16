package com.sparta.users.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailVerificationRequestDto {
    private String email;
    private String verificationCode;
}
