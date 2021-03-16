package com.retro.collegeretro;

import com.retro.collegeretro.Model.Address;
import com.retro.collegeretro.Model.User;
import com.retro.collegeretro.Repository.AddressRepository;
import com.retro.collegeretro.Repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RepositoryTest {

    @Autowired
    UserRepository userRepo;

    @Autowired
    AddressRepository addressRepo;

    @Test
    public void testRun() {
      User user = new User();
      user.setEmail("hello@uga.edu");
      user.setPassword("goDawgs");
      userRepo.save(user);
      assert userRepo.findAll().size() == 1;
    }

    @Test
    public void testRun2() {
        User user = new User();
        user.setEmail("1234@uga.edu");
        user.setPassword("helloWorld");
        Set<Address> addresses = new HashSet<Address>();
        Address address = new Address();
        address.setPrimaryLine("1234 Memory Lane");
        address.setCity("Athens");
        address.setState("Georgia");
        address.setZipCode("12345");
        addresses.add(address);
        user.setAddresses(addresses);
        address.setUser(user);
        userRepo.save(user);
        assert addressRepo.findAll().size() == 1;
    }

    @BeforeAll
    public void delete() {
        userRepo.deleteAll();
    }
}
