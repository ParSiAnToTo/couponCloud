package com.sparta.couponcloud.wishlist.repository;

import com.sparta.couponcloud.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    @Query("SELECT w FROM Wishlist w JOIN FETCH w.product p WHERE w.users.userId = :userId AND p.available = true")
    List<Wishlist> findAllByUserId(Long userId);

}
