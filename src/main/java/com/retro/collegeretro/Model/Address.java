package com.retro.collegeretro.Model;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

}
