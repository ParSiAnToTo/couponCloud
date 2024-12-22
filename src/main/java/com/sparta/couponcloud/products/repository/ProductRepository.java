package com.sparta.couponcloud.products.repository;

import com.sparta.couponcloud.products.entity.Category;
import com.sparta.couponcloud.products.entity.Product;
import com.sparta.couponcloud.products.response.ProductAllListDto;
import com.sparta.couponcloud.products.response.ProductDetailDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT new com.sparta.couponcloud.products.response.ProductAllListDto" +
            "(p.productId, p.productName, p.price, p.image, p.category) " +
            "FROM Product p WHERE p.available = true")
    List<ProductAllListDto> findAllList();

    @Query("SELECT new com.sparta.couponcloud.products.response.ProductAllListDto" +
            "(p.productId, p.productName, p.price, p.image, p.category) " +
            "FROM Product p WHERE p.productName LIKE %:name% AND p.available = true")
    List<ProductAllListDto> findByNameContaining(@Param("name") String name);

    @Query("SELECT new com.sparta.couponcloud.products.response.ProductAllListDto" +
            "(p.productId, p.productName, p.price, p.image, p.category) " +
            "FROM Product p WHERE p.category = :category AND p.available = true")
    List<ProductAllListDto> findByCategory(@Param("category") Category category);

    @Query("SELECT new com.sparta.couponcloud.products.response.ProductAllListDto" +
            "(p.productId, p.productName, p.price, p.image, p.category) " +
            "FROM Product p WHERE p.category = :category AND p.productName LIKE %:name% AND p.available = true")
    List<ProductAllListDto> findByCategoryAndNameContaining(@Param("category") Category category, @Param("name") String name);

    @Query("SELECT new com.sparta.couponcloud.products.response.ProductDetailDto" +
            "(p.productId, p.productName, p.price, p.image, p.description, p.category) " +
            "FROM Product p WHERE p.productId = :productId AND p.available = true")
    Optional<ProductDetailDto> findProductDetailById(@Param("productId") Long productId);
}
