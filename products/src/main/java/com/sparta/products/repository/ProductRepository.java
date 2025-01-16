package com.sparta.products.repository;

import com.sparta.products.entity.Category;
import com.sparta.products.entity.Product;
import com.sparta.products.response.ProductAllListDto;
import com.sparta.products.response.ProductDetailDto;
import com.sparta.products.response.ProductDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT new com.sparta.products.response.ProductAllListDto" +
            "(p.productId, p.productName, p.price, p.image, p.category) " +
            "FROM Product p WHERE p.available = true")
    List<ProductAllListDto> findAllList();

    @Query("SELECT new com.sparta.products.response.ProductAllListDto" +
            "(p.productId, p.productName, p.price, p.image, p.category) " +
            "FROM Product p WHERE p.productName LIKE %:name% AND p.available = true")
    List<ProductAllListDto> findByNameContaining(@Param("name") String name);

    @Query("SELECT new com.sparta.products.response.ProductAllListDto" +
            "(p.productId, p.productName, p.price, p.image, p.category) " +
            "FROM Product p WHERE p.category = :category AND p.available = true")
    List<ProductAllListDto> findByCategory(@Param("category") Category category);

    @Query("SELECT new com.sparta.products.response.ProductAllListDto" +
            "(p.productId, p.productName, p.price, p.image, p.category) " +
            "FROM Product p WHERE p.category = :category AND p.productName LIKE %:name% AND p.available = true")
    List<ProductAllListDto> findByCategoryAndNameContaining(@Param("category") Category category, @Param("name") String name);

    @Query("SELECT new com.sparta.products.response.ProductDetailDto" +
            "(p.productId, p.productName, p.price, p.image, p.description, p.category) " +
            "FROM Product p WHERE p.productId = :productId AND p.available = true")
    Optional<ProductDetailDto> findProductDetailById(@Param("productId") Long productId);

    @Query(value = "SELECT p.product_id AS productId, " +
            "p.product_name AS productName, " +
            "p.price AS price, " +
            "p.image AS image, " +
            "p.available AS active " +
            "FROM product p " +
            "WHERE p.productId = :productId AND p.available = true", nativeQuery = true)
    ProductDto findBySimpleId(@Param("productId") Long productId);
}
