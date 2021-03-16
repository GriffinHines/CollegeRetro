package com.retro.collegeretro.Runners;

import com.retro.collegeretro.Model.Role;
import com.retro.collegeretro.Model.User;
import com.retro.collegeretro.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * This class creates an admin with the username `admin` and the
 * password `password` if it doesn't already exist.
 */
@Slf4j
@Component
public class AdminCreator implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminCreator(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findAllAdmins().size() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setEmail("12345@gmail.com");
            admin.setFName("Admin");
            admin.setLName("Biggs");
            admin.addRole(Role.ADMIN);
            admin.setVerified(true);
            userRepository.save(admin);
            log.info("Created a new admin with username=admin and password=password");
        } else {
            log.info("There is already an admin, so not creating one");
        }
    }
}
