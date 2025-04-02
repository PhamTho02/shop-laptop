package com.example.shop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.shop.domain.Cart;
import com.example.shop.domain.CartDetail;
import com.example.shop.domain.Product;
import com.example.shop.domain.User;
import com.example.shop.repository.CartDetailRepository;
import com.example.shop.repository.CartRepository;
import com.example.shop.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final UserService userService;

    public ProductService(ProductRepository productRepository, CartRepository cartRepository,
            CartDetailRepository cartDetailRepository, UserService userService) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.userService = userService;
    }

    public Product handleSaveProduct(Product product) {
        return this.productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    public Optional<Product> fetchProductById(long id) {
        return this.productRepository.findById(id);
    }

    public void handleDeleteProduct(long id) {
        this.productRepository.deleteById(id);
    }

    public void handleAddProductToCart(String email, long productID) {

        User user = this.userService.getUserByEmail(email);
        if (user != null) {
            // check user have cart
            Cart cart = this.cartRepository.findByUser(user);

            if (cart == null) {
                // create cart
                Cart newCart = new Cart();
                newCart.setUser(user);
                newCart.setSum(1);

                cart = this.cartRepository.save(newCart);
            }
            // save cartDetail

            // find product by id
            Optional<Product> productOptional = this.productRepository.findById(productID);
            if (productOptional.isPresent()) {
                Product realProduct = productOptional.get();

                // check have product exist
                CartDetail oldDetail = this.cartDetailRepository.findByCartAndProduct(cart, realProduct);
                if (oldDetail == null) {
                    CartDetail cartDetail = new CartDetail();

                    cartDetail.setCart(cart);
                    cartDetail.setProduct(realProduct);
                    cartDetail.setPrice(realProduct.getPrice());
                    cartDetail.setQuantity(1);

                    this.cartDetailRepository.save(cartDetail);
                } else {
                    oldDetail.setQuantity(oldDetail.getQuantity() + 1);
                    this.cartDetailRepository.save(oldDetail);
                }

            }

        }
    }
}
