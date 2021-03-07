package com.retro.collegeretro.Model;

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
    private long id;
}
