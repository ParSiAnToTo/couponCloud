package com.sparta.couponcloud.products.controller;

import com.sparta.couponcloud.products.entity.Category;
import com.sparta.couponcloud.products.response.ProductAllListDto;
import com.sparta.couponcloud.products.response.ProductDetailDto;
import com.sparta.couponcloud.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    public final ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllProducts() {
        List<ProductAllListDto> allList = productService.getAllProducts();
        return ResponseEntity.ok(allList);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProductsByName(@RequestParam("name") String name) {
        List<ProductAllListDto> products = productService.searchProductsByName(name);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable("category") Category category) {
        List<ProductAllListDto> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{category}/search")
    public ResponseEntity<?> searchProductsByCategoryAndName(@PathVariable("category") Category category,
                                                             @RequestParam("name") String name) {
        List<ProductAllListDto> products = productService.searchProductsByCategoryAndName(category, name);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable("productId") Long productId) {
        ProductDetailDto product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }
}
