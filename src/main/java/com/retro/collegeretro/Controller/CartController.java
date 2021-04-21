package com.retro.collegeretro.Controller;

import com.retro.collegeretro.Model.Cart;
import com.retro.collegeretro.Model.Listing;
import com.retro.collegeretro.Model.User;
import com.retro.collegeretro.Repository.ListingRepository;
import com.retro.collegeretro.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Set;

@Controller
@RequestMapping("/account/cart")
public class CartController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ListingRepository listingRepository;

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

    @GetMapping("/remove/{listingId}")
    public RedirectView removeCartItem(@PathVariable Long listingId, @SessionAttribute User user) {
        user = userRepository.findById(user.getUserId()).get();
        user.getCart().getListings().removeIf(listing -> listing.getListingId() == listingId);
        userRepository.save(user);

        Listing listing = listingRepository.findById(listingId).get();
        listing.setQuantity(listing.getQuantity() + 1);
        listingRepository.save(listing);

        return new RedirectView("/account/cart?remove");
    }

}
