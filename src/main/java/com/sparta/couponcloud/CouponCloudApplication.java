package com.sparta.couponcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CouponCloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(CouponCloudApplication.class, args);
	}

}
