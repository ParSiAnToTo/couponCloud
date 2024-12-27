package com.sparta.couponcloud.users.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DataUpdateRequestDto {
    private String phoneNumber;
    private String address;
    private String name;
}
