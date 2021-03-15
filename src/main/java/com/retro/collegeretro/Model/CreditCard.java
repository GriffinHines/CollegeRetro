package com.retro.collegeretro.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Belongs to one user. A user can have up to 3 cards saved.
 */
@Data
@NoArgsConstructor
@Entity
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String nameOnTheCard;
    private String cardIssuing;
    private long cardNumber;
    private int expiration;
    private int cvcNumber;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return nameOnTheCard;
    }

    public void setName(String nameOnTheCard) {
        this.nameOnTheCard = nameOnTheCard;
    }

    public String getCardIssuing() {
        return cardIssuing;
    }

    public void setCardIssuing(String cardIssuing) {
        this.cardIssuing = cardIssuing;
    }

    public long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getExpiration() {
        return expiration;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }

    public int getCvcNumber() {
        return cvcNumber;
    }

    public void setCvcNumber(int cvcNumber) {
        this.cvcNumber = cvcNumber;
    }
}
