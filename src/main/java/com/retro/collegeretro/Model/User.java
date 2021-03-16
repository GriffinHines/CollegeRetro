package com.retro.collegeretro.Model;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.RandomStringUtils;

import javax.persistence.*;
import java.util.*;

/**
 * A user is an admin or regular user who can log on to the website.
 */
@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userId;
    private String fName;
    private String lName;
    @NotNull
    private String email;
    @NotNull
    private String username;
    @NotNull
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Address> addresses = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<CreditCard> creditCards = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Listing> listings = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<UserRole> roleList = new ArrayList<>();

    private boolean active = true;      // If they haven't closed their account
    private boolean expired = false;    // Not used, but needed for security module
    private boolean verified = false;   // If they verified their email
    private String verificationToken;

    public User() {
        roleList.add(new UserRole(this, Role.USER));
        setNewVerificationToken();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId && Objects.equals(fName, user.fName) && Objects.equals(lName, user.lName) && email.equals(user.email) && username.equals(user.username) && password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, fName, lName, email, username, password);
    }

    public void setNewVerificationToken() {
        verificationToken = RandomStringUtils.random(32, true, true);
    }

    public void addRole(Role role) {
        roleList.add(new UserRole(this, role));
    }
}
