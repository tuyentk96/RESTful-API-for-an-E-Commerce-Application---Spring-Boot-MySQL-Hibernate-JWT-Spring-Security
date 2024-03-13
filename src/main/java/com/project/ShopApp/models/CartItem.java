package com.project.ShopApp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_item")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity",nullable = false)
    private Integer quantity;

    @Column(name = "color",length = 20)
    private String color;

    @ManyToOne
    @JoinColumn(name = "shopping_cart_id")
    private ShoppingCart shoppingCart;
}
