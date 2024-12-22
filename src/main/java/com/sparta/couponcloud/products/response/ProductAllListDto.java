package com.sparta.couponcloud.products.response;

import com.sparta.couponcloud.products.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductAllListDto {
    private Long productId;
    private String productName;
    private Long price;
    private String image;
    private Category category;
}
