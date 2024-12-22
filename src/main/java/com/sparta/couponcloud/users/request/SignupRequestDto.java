package com.sparta.couponcloud.users.request;

import lombok.Getter;

@Getter
public class SignupRequestDto {
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private String name;
}
