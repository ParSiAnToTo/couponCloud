package com.sparta.wishlist.controller;

import com.sparta.wishlist.request.UpdateQuantityRequestDto;
import com.sparta.wishlist.response.WishlistDto;
import com.sparta.wishlist.response.WishlistResponseDto;
import com.sparta.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping("/get")
    public ResponseEntity<List<WishlistDto>> getWishlist(@RequestHeader("X-User-Id") Long userId) {
        log.info("X-User-Id Header Value: {}", userId);
        List<WishlistDto> wishlist = wishlistService.getWishlist(userId);
        return ResponseEntity.ok(wishlist);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToWishlist(@RequestParam Long productId,
                                           @RequestParam int quantity,
                                           @RequestHeader("X-User-Id") Long userId) {
        wishlistService.addToWishlist(productId, quantity, userId);
        return ResponseEntity.ok("Item added to wishlist");
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateWishlistQuantity(@RequestBody UpdateQuantityRequestDto updateQuantityRequestDto,
                                                         @RequestHeader("X-User-Id") Long userId) {
        wishlistService.updateQuantity(updateQuantityRequestDto, userId);
        return ResponseEntity.ok("Quantity updated successfully");
    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeFromWishlist(@RequestParam Long wishId,
                                                     @RequestHeader("X-User-Id") Long userId) {
        wishlistService.removeFromWishlist(wishId);
        return ResponseEntity.ok("Item removed from wishlist");
    }

    @GetMapping("/getlist")
    public ResponseEntity<List<WishlistResponseDto>> getWishlistResponse(@RequestParam List<Long> wishlistIds) {
        List<WishlistResponseDto> wishlists = wishlistService.getWishlistResponse(wishlistIds);
        return ResponseEntity.ok(wishlists);
    }

    @PostMapping("/removeOrder")
    public ResponseEntity<Void> removeWishlists(@RequestBody List<Long> wishlistIds) {
        wishlistService.removeWishlists(wishlistIds);
        return ResponseEntity.noContent().build();
    }
}

