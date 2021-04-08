package com.retro.collegeretro.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

/**
 * A cart is a list of listings belonging to a person who is trying to
 * purchase them. Each person can only have one cart.
 */
@Data
@NoArgsConstructor
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long cartId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Listing> listings = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return cartId == cart.cartId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartId);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + cartId +
                ", listings=" + listings +
                '}';
    }
}

