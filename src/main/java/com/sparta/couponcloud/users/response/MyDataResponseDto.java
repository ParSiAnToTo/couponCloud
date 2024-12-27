package com.sparta.couponcloud.users.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyDataResponseDto {

    private String email;
    private String phoneNumber;
    private String address;
    private String name;

}
