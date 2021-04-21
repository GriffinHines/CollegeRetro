package com.retro.collegeretro.Controller;

import com.retro.collegeretro.Model.Address;
import com.retro.collegeretro.Model.CreditCard;
import com.retro.collegeretro.Model.Listing;
import com.retro.collegeretro.Model.User;
import com.retro.collegeretro.Repository.UserRepository;
import com.retro.collegeretro.Service.MyEmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

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
                                    HttpServletRequest request,
                                    RedirectAttributes attributes) {
        // Can't make an account if logged in
        if (user != null) return new RedirectView("/");

        // Make sure email and username are unique
        if (userRepository.findByUsername(username) != null) {
            attributes.addAttribute("error", true);
            attributes.addAttribute("errorMsg", "Error: There is already an account with that username.");
            return new RedirectView("/account/create");
        } else if (userRepository.findByEmail(email) != null) {
            attributes.addAttribute("error", true);
            attributes.addAttribute("errorMsg", "Error: There is already an account with that email.");
            return new RedirectView("/account/create");
        }

        // Create the account
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(newUser);

        // Email a verification link to the new user
        myEmailSender.setSubject("Verify your email address");
        String verifyLink = "http://" + domainRoot + "/account/verify?token=" + newUser.getVerificationToken();
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
    public String getLoginPage(Model model) {
        return "login";
    }

    @GetMapping("/profile")
    @Transactional
    public String getUserProfile(@SessionAttribute User user, Model model) {
        // Accommodate lazy fetching
        user = userRepository.findById(user.getUserId()).get();
        model.addAttribute("addresses", user.getAddresses());
        model.addAttribute("cards", user.getCreditCards());
        model.addAttribute("listings", user.getListings());
        return "profile";
    } // getUserProfile

    @PostMapping("/user/username")
    public RedirectView updateUsername(@RequestParam String username, @SessionAttribute User user) {
        if (userRepository.findByUsername(username) == null) {
            // change only if username is not already taken
            user.setUsername(username);
            userRepository.save(user);
        } // if
        return new RedirectView("/account/profile");
    } // updateUsername

    @PostMapping("/user/card/add")
    public RedirectView addCard(@RequestParam String name, @RequestParam String num,
                                @RequestParam int month, @RequestParam int year, @RequestParam int cvv,
                                @SessionAttribute User user) {
        user = userRepository.findById(user.getUserId()).get();
        CreditCard card = new CreditCard(name, num, month, year, cvv, user);
        user.getCreditCards().add(card);
        userRepository.save(user);
        return new RedirectView("/account/profile");
    }

    @PostMapping("/user/address/add")
    public RedirectView addAddress(@RequestParam String line1, @RequestParam(required = false) String line2,
                                   @RequestParam String city, @RequestParam String state, @RequestParam String zip,
                                   @SessionAttribute User user) {
        user = userRepository.findById(user.getUserId()).get();
        Address address = new Address(line1, line2, zip, city, state, user);
        user.getAddresses().add(address);
        userRepository.save(user);
        return new RedirectView("/account/profile");
    }

    @GetMapping("/user/card/delete")
    public RedirectView deleteCard(@RequestParam Long id, @SessionAttribute User user) {
        user = userRepository.findById(user.getUserId()).get();
        user.getCreditCards().removeIf(creditCard -> creditCard.getCreditCardId() == id);
        userRepository.save(user);
        return new RedirectView("/account/profile");
    }

    @GetMapping("/user/address/delete")
    public RedirectView deleteAddress(@RequestParam Long id, @SessionAttribute User user) {
        user = userRepository.findById(user.getUserId()).get();
        user.getAddresses().removeIf(addr -> addr.getAddressId() == id);
        userRepository.save(user);
        return new RedirectView("/account/profile");
    }

    @GetMapping("/listing")
    public String getNewListingPage() {
        return "new-listing";
    }

    @PostMapping("/listing")
    public RedirectView makeNewListing(@RequestParam String title, @RequestParam double price,
                                 @RequestParam Integer quantity, @RequestParam String category,
                                 @RequestParam String description, @RequestParam String image,
                                 @SessionAttribute User user) {
        user = userRepository.findById(user.getUserId()).get();
        Listing listing = new Listing(category, title, (int)(price * 100), description, quantity, image, user);
        user.getListings().add(listing);
        userRepository.save(user);
        return new RedirectView("/account/profile?newListing#listings");
    }

    @GetMapping("/listing/close/{listingId}")
    public RedirectView closeListing(@PathVariable Long listingId, @SessionAttribute User user) {
        user = userRepository.findById(user.getUserId()).get();
        user.getListings().forEach(listing -> {
            if (listing.getListingId() == listingId) {
                listing.setOpen(false);
            }
        });
        userRepository.save(user);
        return new RedirectView("/account/profile?closeListing#listings");
    }

}