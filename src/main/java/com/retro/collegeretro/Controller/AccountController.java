package com.retro.collegeretro.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/create")
    public String makeAccountPage(){
        return "create-account";
    }

    @PostMapping("/create")
    public String makeAccount(@RequestParam String email, @RequestParam String username, @RequestParam String password){
        return null;
    }

}