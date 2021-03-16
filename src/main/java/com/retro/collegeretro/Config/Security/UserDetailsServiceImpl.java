package com.retro.collegeretro.Config.Security;

import com.retro.collegeretro.Model.User;
import com.retro.collegeretro.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * A custom implementation of the UserDetailsService
 * interface which allows finding a user by their username
 * using my specific database setup.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail);

        // If not found, raise exception
        if (user == null) {
            throw new UsernameNotFoundException("Could not find account with username or email " + usernameOrEmail);
        }

        return new UserDetailsImpl(user);
    }
}
