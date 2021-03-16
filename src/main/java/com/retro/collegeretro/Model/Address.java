package com.retro.collegeretro.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

/**
 * Belongs to one user. A user can have up to 3 addresses saved.
 * This contains a required line one, an optional line 2, a zip
 * (10 digits), and a state.
 */
@Data
@NoArgsConstructor
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long addressId;
    @NotNull
    private String primaryLine;
    private String optionalLine;
    @NotNull
    private String zipCode;
    @NotNull
    private String city;
    @NotNull
    private String state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    public Address(String primaryLine, String zipCode, String city, String state, User user) {
        this.primaryLine = primaryLine;
        this.zipCode = zipCode;
        this.city = city;
        this.state = state;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressId=" + addressId +
                ", primaryLine='" + primaryLine + '\'' +
                ", optionalLine='" + optionalLine + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", user=" + user.getUserId() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return addressId == address.addressId && primaryLine.equals(address.primaryLine) && Objects.equals(optionalLine, address.optionalLine) && zipCode.equals(address.zipCode) && city.equals(address.city) && state.equals(address.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(addressId, primaryLine, optionalLine, zipCode, city, state);
    }
}
