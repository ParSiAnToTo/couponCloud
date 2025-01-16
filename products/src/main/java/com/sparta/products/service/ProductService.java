package com.sparta.products.service;

import com.sparta.products.entity.Category;
import com.sparta.products.repository.ProductRepository;
import com.sparta.products.response.ProductAllListDto;
import com.sparta.products.response.ProductDetailDto;
import com.sparta.products.response.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public ProductDto getProductDtoById(Long productId) {
        return productRepository.findBySimpleId(productId);
    }

    public List<ProductDto> getProductsByIds(List<Long> productIds) {
        return productRepository.findAllById(productIds).stream()
                .map(product -> new ProductDto(
                        product.getProductId(),
                        product.getProductName(),
                        product.getPrice(),
                        product.getImage(),
                        product.isAvailable()
                ))
                .collect(Collectors.toList());
    }
}
