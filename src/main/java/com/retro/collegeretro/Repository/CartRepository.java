package com.retro.collegeretro.Repository;

import com.retro.collegeretro.Model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
