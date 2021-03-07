package com.retro.collegeretro.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    private long id;
}
