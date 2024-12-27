package com.sparta.couponcloud.users.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PasswordUpdateRequestDto {
    private String currentPassword;
    private String newPassword;
}
