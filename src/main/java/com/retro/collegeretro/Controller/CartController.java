package com.retro.collegeretro.Controller;

import com.retro.collegeretro.Model.Cart;
import com.retro.collegeretro.Model.Listing;
import com.retro.collegeretro.Model.User;
import com.retro.collegeretro.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.Set;

@Controller
@RequestMapping("/account/cart")
public class CartController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String getCartPage(Model model, @SessionAttribute User user) {
        // Add cart listings
        user = userRepository.findById(user.getUserId()).get();
        final Set<Listing> listings = user.getCart().getListings();
        model.addAttribute("cart", listings);

        // Add subtotal
        int subtotalCents = 0;
        for (Listing listing : listings) {
            subtotalCents += listing.getPriceInCents();
        }
        model.addAttribute("subtotal", (double)(subtotalCents) / 100);

        return "cart";
    }

}
