package com.retro.collegeretro.Model;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Belongs to one user. A user can have up to 3 cards saved.
 */
@Data
@NoArgsConstructor
@Entity
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long creditCardId;
    @NotNull
    private String nameOnTheCard;
    @NotNull
    private String cardIssuing;
    @NotNull
    private long cardNumber;
    @NotNull
    private int expiration;
    @NotNull
    private int cvcNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;
}
