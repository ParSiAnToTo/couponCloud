package com.sparta.couponcloud.wishlist.controller;

import com.sparta.couponcloud.wishlist.request.UpdateQuantityRequestDto;
import com.sparta.couponcloud.wishlist.response.WishlistDto;
import com.sparta.couponcloud.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping("/get")
    public ResponseEntity<List<WishlistDto>> getWishlist() {
        List<WishlistDto> wishlist = wishlistService.getWishlist();
        return ResponseEntity.ok(wishlist);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToWishlist(@RequestParam Long productId, @RequestParam int quantity) {
        wishlistService.addToWishlist(productId, quantity);
        return ResponseEntity.ok("Item added to wishlist");
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateWishlistQuantity(@RequestBody UpdateQuantityRequestDto updateQuantityRequestDto) {
        wishlistService.updateQuantity(updateQuantityRequestDto);
        return ResponseEntity.ok("Quantity updated successfully");
    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeFromWishlist(@RequestParam Long wishId) {
        wishlistService.removeFromWishlist(wishId);
        return ResponseEntity.ok("Item removed from wishlist");
    }
}
