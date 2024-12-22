package com.sparta.couponcloud.wishlist.service;

import com.sparta.couponcloud.products.entity.Product;
import com.sparta.couponcloud.products.repository.ProductRepository;
import com.sparta.couponcloud.products.service.ProductService;
import com.sparta.couponcloud.users.entity.Users;
import com.sparta.couponcloud.users.repository.UsersRepository;
import com.sparta.couponcloud.wishlist.entity.Wishlist;
import com.sparta.couponcloud.wishlist.repository.WishlistRepository;
import com.sparta.couponcloud.wishlist.request.UpdateQuantityRequestDto;
import com.sparta.couponcloud.wishlist.response.WishlistDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final UsersRepository usersRepository;

    @Transactional(readOnly = true)
    public List<WishlistDto> getWishlist() {
        Long userId = getAuthenticatedUserId();

        List<Wishlist> wishlists = wishlistRepository.findAllByUserId(userId);

        return wishlists.stream()
                .filter(wishlist -> wishlist.getProduct().isAvailable())
                .map(wishlist -> new WishlistDto(
                        wishlist.getWishId(),
                        wishlist.getProduct().getProductId(),
                        wishlist.getProduct().getProductName(),
                        wishlist.getProduct().getPrice(),
                        wishlist.getQuantity()
                ))
                .toList();
    }

    @Transactional
    public void addToWishlist(Long productId, int quantity) {
        Long userId = getAuthenticatedUserId();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Wishlist wishlist = new Wishlist(user, product, quantity);
        wishlistRepository.save(wishlist);
    }

    @Transactional
    public void updateQuantity(UpdateQuantityRequestDto updateQuantityRequestDto) {
        Long userId = getAuthenticatedUserId();

        if (updateQuantityRequestDto.getQuantity() <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }
        Wishlist wishlist = wishlistRepository.findById(updateQuantityRequestDto.getWishId())
                .orElseThrow(() -> new IllegalArgumentException("Wishlist item not found"));

        if (!wishlist.getProduct().isAvailable() || !wishlist.getUsers().getUserId().equals(userId)) {
            throw new IllegalArgumentException("Invalid Request");
        }

        wishlist.updateQuantity(updateQuantityRequestDto.getQuantity());
    }

    @Transactional
    public void removeFromWishlist(Long wishId) {
        Long userId = getAuthenticatedUserId();

        Wishlist wishlist = wishlistRepository.findById(wishId)
                .orElseThrow(() -> new IllegalArgumentException("Wishlist item not found"));

        if (!wishlist.getUsers().getUserId().equals(userId)) {
            throw new IllegalArgumentException("Wishlist item is not owned by user");
        }

        wishlistRepository.delete(wishlist);
    }

    private Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }
        return (Long) authentication.getPrincipal();
    }
}
