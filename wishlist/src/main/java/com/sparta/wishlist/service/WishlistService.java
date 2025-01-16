package com.sparta.wishlist.service;

import com.sparta.wishlist.response.ProductDto;
import com.sparta.wishlist.response.WishlistResponseDto;
import com.sparta.wishlist.entity.Wishlist;
import com.sparta.wishlist.repository.WishlistRepository;
import com.sparta.wishlist.request.UpdateQuantityRequestDto;
import com.sparta.wishlist.response.WishlistDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final RestTemplate restTemplate;

    @Transactional(readOnly = true)
    public List<WishlistDto> getWishlist(Long userId) {
        List<Wishlist> wishlists = wishlistRepository.findAllByUserId(userId);

        List<Long> productIds = wishlists.stream()
                .map(Wishlist::getProductId)
                .distinct()
                .collect(Collectors.toList());

        String url = "http://localhost:8080/product/multicall";
        ProductDto[] productDtos = restTemplate.postForObject(url, productIds, ProductDto[].class);
        Map<Long, ProductDto> productMap = List.of(productDtos).stream()
                .collect(Collectors.toMap(ProductDto::getProductId, product -> product));

        return wishlists.stream()
                .map(wishlist -> {
                    ProductDto product = productMap.get(wishlist.getProductId());
                    if (product == null) {
                        throw new IllegalArgumentException("Product not found for ID: " + wishlist.getProductId());
                    }
                    return new WishlistDto(
                            wishlist.getWishId(),
                            wishlist.getProductId(),
                            product.getProductName(),
                            product.getPrice(),
                            wishlist.getQuantity()
                    );
                })
                .collect(Collectors.toList());

    }

    @Transactional
    public void addToWishlist(Long productId, int quantity, Long userId) {
        String url = "http://localhost:8080/product/{productId}";
        ProductDto product = restTemplate.getForObject(url, ProductDto.class, productId);

        Wishlist wishlist = new Wishlist(userId, product.getProductId(), quantity);
        wishlistRepository.save(wishlist);
    }

    @Transactional
    public void updateQuantity(UpdateQuantityRequestDto updateQuantityRequestDto, Long userId) {
        if (updateQuantityRequestDto.getQuantity() <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }
        Wishlist wishlist = wishlistRepository.findById(updateQuantityRequestDto.getWishId())
                .orElseThrow(() -> new IllegalArgumentException("Wishlist item not found"));

        wishlist.updateQuantity(updateQuantityRequestDto.getQuantity());
    }

    @Transactional
    public void removeFromWishlist(Long wishId) {
        Wishlist wishlist = wishlistRepository.findById(wishId)
                .orElseThrow(() -> new IllegalArgumentException("Wishlist item not found"));

        wishlistRepository.delete(wishlist);
    }

    @Transactional
    public List<WishlistResponseDto> getWishlistResponse(List<Long> wishlistIds) {
        return wishlistRepository.findAllById(wishlistIds)
                .stream()
                .map(wishlist -> new WishlistResponseDto(
                        wishlist.getWishId(),
                        wishlist.getProductId(),
                        wishlist.getQuantity()))
                .toList();
    }

    public void removeWishlists(List<Long> wishlistIds) {
        wishlistRepository.deleteAllById(wishlistIds);
    }
}


