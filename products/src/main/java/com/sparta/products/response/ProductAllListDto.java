package com.sparta.products.response;

import com.sparta.products.entity.Category;
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
