package com.retro.collegeretro;

import com.retro.collegeretro.Model.Address;
import com.retro.collegeretro.Model.CreditCard;
import com.retro.collegeretro.Model.User;
import com.retro.collegeretro.Repository.AddressRepository;
import com.retro.collegeretro.Repository.CreditCardRepository;
import com.retro.collegeretro.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;

    @Test
    public void testCreateUser() {
      User user = new User();
      user.setEmail("hello@uga.edu");
      user.setPassword("goDawgs");
      userRepository.save(user);
      assert userRepository.findAll().size() == 1;
    }

    @Test
    public void testAddAddressToUser() {
        User user = new User();
        user.setEmail("1234@uga.edu");
        user.setPassword("helloWorld");
        user.getAddresses().add(new Address("1234 Memory Lane", "12345-5678", "Athens", "GA", user));
        userRepository.save(user);
        assert addressRepository.findAll().size() == 1;
    }

    @Test
    public void testAddCreditCardToUser() {
        User user = new User();
        user.setEmail("1234@uga.edu");
        user.setPassword("helloWorld");
        user.getCreditCards().add(new CreditCard("Danny Gelber", "128096434142", 5, 2022, 123, user));
        userRepository.save(user);
        assert creditCardRepository.findAll().size() == 1;
    }

    @Test
    public void testAddressCascadingDelete() {
        User user = new User();
        user.setEmail("1234@uga.edu");
        user.setPassword("helloWorld");
        user.getAddresses().add(new Address("1234 Memory Lane", "12345-5678", "Athens", "GA", user));
        user = userRepository.save(user);
        userRepository.deleteById(user.getUserId());
        assert addressRepository.findAll().size() == 0;
    }

    @BeforeEach
    public void delete() {
        userRepository.deleteAll();
    }

}
