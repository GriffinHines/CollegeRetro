package com.retro.collegeretro.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * An entity class which stores the roles for a user.
 */
@Data
@NoArgsConstructor
@Entity(name = "user_role")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Enumerated(EnumType.ORDINAL)
    private Role role;

    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
    }
}
