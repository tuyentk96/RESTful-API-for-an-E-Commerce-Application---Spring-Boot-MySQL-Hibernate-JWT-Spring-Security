package com.project.ShopApp.services.impl;

import com.project.ShopApp.dto.request.CartItemRequest;
import com.project.ShopApp.dto.respone.ShoppingCartResponse;
import com.project.ShopApp.exception.ErrorResponse;
import com.project.ShopApp.exception.ErrorResult;
import com.project.ShopApp.models.CartItem;
import com.project.ShopApp.models.Product;
import com.project.ShopApp.models.ShoppingCart;
import com.project.ShopApp.repositories.CartItemRepository;
import com.project.ShopApp.repositories.ProductRepository;
import com.project.ShopApp.repositories.ShoppingCartRepository;
import com.project.ShopApp.repositories.UserRepository;
import com.project.ShopApp.services.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    @Override
    public ShoppingCartResponse addCart(Long userId, CartItemRequest cartItemRequest) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId);
        if (shoppingCart==null){
            shoppingCart = ShoppingCart.builder()
                    .user(userRepository.findById(userId)
                            .orElseThrow(()->new ErrorResponse(ErrorResult.NOT_FOUND_USER)))
                    .build();

            shoppingCartRepository.saveAndFlush(shoppingCart);

            List<CartItem> itemList = new ArrayList<>();

            CartItem cartItem = CartItem.builder()
                    .shoppingCart(shoppingCart)
                    .color(cartItemRequest.getColor())
                    .quantity(cartItemRequest.getQuantity())
                    .product(productRepository.findById(cartItemRequest.getProductId())
                            .orElseThrow(()->new ErrorResponse(ErrorResult.NOT_FOUND_DATA)))
                    .build();
            itemList.add(cartItem);

            cartItemRepository.save(cartItem);

            return ShoppingCartResponse.builder()
                    .userId(userId)
                    .itemList(itemList)
                    .build();

        }else {
            List<CartItem> itemList = cartItemRepository.findAllByShoppingCartId(shoppingCart.getId());

            Product product = productRepository.findById(cartItemRequest.getProductId())
                    .orElseThrow(()->new ErrorResponse(ErrorResult.NOT_FOUND_DATA));

            for (CartItem cartItem : itemList){
                Product existingProduct = cartItem.getProduct();
                if (product==existingProduct){

                    cartItem = cartItemRepository.findByShoppingCartIdAndProductId(shoppingCart.getId(), cartItemRequest.getProductId());
                    cartItem.setColor(cartItemRequest.getColor());
                    cartItem.setQuantity(cartItemRequest.getQuantity());

                    cartItemRepository.save(cartItem);

                    return ShoppingCartResponse.builder()
                            .userId(userId)
                            .itemList(itemList)
                            .build();
                }
            }

            CartItem cartItem = CartItem.builder()
                    .shoppingCart(shoppingCart)
                    .color(cartItemRequest.getColor())
                    .quantity(cartItemRequest.getQuantity())
                    .product(product)
                    .build();

            cartItemRepository.save(cartItem);

            itemList.add(cartItem);

            return ShoppingCartResponse.builder()
                    .userId(userId)
                    .itemList(itemList)
                    .build();
        }
    }

    @Override
    public void deleteCart(Long shoppingCartId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(shoppingCartId)
                .orElseThrow(()-> new ErrorResponse(ErrorResult.NOT_FOUND_DATA));
        List<CartItem> cartItems = cartItemRepository.findAllByShoppingCartId(shoppingCartId);

        cartItemRepository.deleteAll(cartItems);

        shoppingCartRepository.delete(shoppingCart);
    }

    @Override
    public List<CartItem> getListCartItem(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId);
        return cartItemRepository.findAllByShoppingCartId(shoppingCart.getId());
    }

}
