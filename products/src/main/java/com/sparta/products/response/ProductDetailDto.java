package com.sparta.products.response;

import com.sparta.products.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductDetailDto {
    private Long productId;
    private String productName;
    private Long price;
    private String image;
    private String description;
    private Category category;
}
