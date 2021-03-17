package com.retro.collegeretro.Controller;

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
    public String getUserProfile(Model model, @SessionAttribute User user) {
        model.addAttribute("userkey", user);
        return "myprofile";
    } // getUserProfile

    @PostMapping("/user/username")
    public RedirectView updateUsername(@RequestParam String username, @SessionAttribute User user) {
        // TODO(michaelrehman): do not allow usernames that equal a preexisting username
        user.setUsername(username);
        userRepository.save(user);
        return new RedirectView("/account/user/myprofile");
    } // updateUsername

}