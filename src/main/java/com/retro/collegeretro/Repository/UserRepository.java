package com.retro.collegeretro.Repository;

import com.retro.collegeretro.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByVerificationToken(String token);

    @Query(value = "select * from user where username = ?1 or email = ?1", nativeQuery = true)
    User findByUsernameOrEmail(String usernameOrEmail);

    @Query(value = "select user.* from user join user_role using (user_id) where role = 2", nativeQuery = true)
    List<User> findAllAdmins();
}
