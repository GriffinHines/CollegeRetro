package com.retro.collegeretro.Controller;

import com.retro.collegeretro.Model.Address;
import com.retro.collegeretro.Model.CreditCard;
import com.retro.collegeretro.Model.Listing;
import com.retro.collegeretro.Model.User;
import com.retro.collegeretro.Repository.UserRepository;
import com.retro.collegeretro.Service.MyEmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

/**
 * This controller contains the endpoints for anything a user
 * might do with their account. Examples are getting account-based
 * webpages, adding an address, deleting a credit card, verifying
 * an email, etc.
 */
@Controller
@Slf4j
@RequestMapping("/account")
public class AccountController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MyEmailSender myEmailSender;

    @Value("${college.retro.domain}")
    private String domainRoot;

    public AccountController(PasswordEncoder passwordEncoder,
                             UserRepository userRepository,
                             MyEmailSender myEmailSender) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.myEmailSender = myEmailSender;
    }

    @GetMapping("/create")
    public String getCreateAccountPage() {
        return "create-account";
    }

    @PostMapping("/create")
    public RedirectView makeAccount(@RequestParam String email,
                                    @RequestParam String username,
                                    @RequestParam String password,
                                    @SessionAttribute(required = false) User user,
                                    HttpServletRequest request) {
        // Can't make an account if logged in
        if (user != null) return new RedirectView("/");

        // Create the account
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(newUser);

        // Email a verification link to the new user
        myEmailSender.setSubject("Verify your email address");
        String verifyLink = "http://" + domainRoot + "account/verify?token=" + newUser.getVerificationToken();
        myEmailSender.sendTo(newUser.getEmail(), verifyLink);

        // Log the user in to their new account
        try {
            request.login(email, password);
        } catch (ServletException ex) {
            return new RedirectView("/account/login?error");
        }
        return new RedirectView("/account/verification");
    }

    @GetMapping("/verify")
    public String getVerificationPage(@RequestParam String token, Model model) {
        User user = userRepository.findByVerificationToken(token);

        // If no user found for the token or the token is too short, don't verify
        if (user == null || token.length() <= 1) {
            model.addAttribute("error", true);

        }
        // Verify user
         else {
            model.addAttribute("error", false);
            user.setVerified(true);
            user.setVerificationToken("");
            userRepository.save(user);
        }

        return "verify";
    }

    @GetMapping("/verification")
    public String getVerificationPromptPage(Model model, @SessionAttribute User user) {
        model.addAttribute("userkey",user);
        return "verify-prompt";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/user/myprofile")
    @Transactional
    public String getUserProfile(Model model, @SessionAttribute User user) {
        // sample test code (not saved to database)
        Set<CreditCard> cards = new HashSet<>();
        cards.add(new CreditCard("Test", "489498489", 3, 8151, 555, user));
        cards.add(new CreditCard("Test", "489498480", 11, 8151, 555, user));
        user.setCreditCards(cards);
        Set<Address> addresses = new HashSet<>();
        addresses.add(new Address("123 Imaginary Lane", "12345", "City", "State", user));
        addresses.add(new Address("1234 Imaginary Lane", "12345", "City", "State", user));
        user.setAddresses(addresses);
        Listing listing = new Listing();
        listing.setListingName("name");
        listing.setCategory("cate");
        listing.setDescription("some desc");
        listing.setOpen(true);
        listing.setPriceInCents(123);
        listing.setQuantity(12);
        List<Listing> listings = new ArrayList<>();
        listings.add(listing);
        user.setListings(listings);

        model.addAttribute("userkey", user);
        return "myprofile";
    } // getUserProfile

    @PostMapping("/user/username")
    public RedirectView updateUsername(@RequestParam String username, @SessionAttribute User user) {
        if (userRepository.findByUsername(username) == null) {
            // change only if username is not already taken
            user.setUsername(username);
            userRepository.save(user);
        } // if
        return new RedirectView("/account/user/myprofile");
    } // updateUsername

}