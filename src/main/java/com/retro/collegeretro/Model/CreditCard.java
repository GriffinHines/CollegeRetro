package com.retro.collegeretro.Model;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

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
    private String nameOnCard;
    @NotNull
    private String cardNumber;
    @NotNull
    private int expMonth;
    @NotNull
    private int expYear;
    @NotNull
    private int cvv;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    public CreditCard(String nameOnCard, String cardNumber, int expMonth, int expYear, int cvv, User user) {
        this.nameOnCard = nameOnCard;
        this.cardNumber = cardNumber;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.cvv = cvv;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditCard that = (CreditCard) o;
        return creditCardId == that.creditCardId && cardNumber == that.cardNumber && expMonth == that.expMonth && expYear == that.expYear && cvv == that.cvv && nameOnCard.equals(that.nameOnCard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creditCardId, nameOnCard, cardNumber, expMonth, expYear, cvv);
    }
}
