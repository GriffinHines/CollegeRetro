package com.retro.collegeretro.Repository;

import com.retro.collegeretro.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
