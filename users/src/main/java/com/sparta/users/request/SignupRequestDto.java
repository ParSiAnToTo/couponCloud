package com.sparta.users.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupRequestDto {
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private String name;
    private boolean emailVerified;
}
