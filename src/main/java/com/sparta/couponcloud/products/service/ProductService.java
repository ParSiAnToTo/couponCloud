package com.sparta.couponcloud.products.service;

import com.sparta.couponcloud.products.entity.Category;
import com.sparta.couponcloud.products.repository.ProductRepository;
import com.sparta.couponcloud.products.response.ProductAllListDto;
import com.sparta.couponcloud.products.response.ProductDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductAllListDto> getAllProducts() {
        return productRepository.findAllList();
    }

    public List<ProductAllListDto> searchProductsByName(String name) {
        return productRepository.findByNameContaining(name);
    }

    public List<ProductAllListDto> getProductsByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    public List<ProductAllListDto> searchProductsByCategoryAndName(Category category, String name) {
        return productRepository.findByCategoryAndNameContaining(category, name);
    }

    public ProductDetailDto getProductById(Long productId) {
        return productRepository.findProductDetailById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));
    }

}
