package com.sparta.wishlist.repository;

import com.sparta.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    @Query("SELECT w FROM Wishlist w WHERE w.userId = :userId")
    List<Wishlist> findAllByUserId(Long userId);
}
