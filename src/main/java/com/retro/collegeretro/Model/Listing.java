package com.retro.collegeretro.Model;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * A listing is an item which some user is trying to sell. A listing
 * belongs to the person who is trying to sell it. There is only ever
 * one item in the listing, so once it is purchased, it is marked as
 * sold and no longer displayed.
 */
@Data
@NoArgsConstructor
@Entity
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long listingId;
    @NotNull
    private String category;
    @NotNull
    private String listingName;
    @NotNull
    private int pennies;
    @NotNull
    private boolean isOpen;
    private String description;
    @NotNull
    private int quantity;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "cartId", nullable = false)
    private List<Cart> carts;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "transactionId", nullable = false)
    private List<Transaction> transactions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

}
