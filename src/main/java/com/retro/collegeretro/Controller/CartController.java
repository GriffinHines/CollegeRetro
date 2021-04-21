package com.retro.collegeretro.Controller;

import com.retro.collegeretro.Model.Cart;
import com.retro.collegeretro.Model.Listing;
import com.retro.collegeretro.Model.User;
import com.retro.collegeretro.Repository.ListingRepository;
import com.retro.collegeretro.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
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
        listing.getCarts().remove(user.getCart());
        listingRepository.save(listing);

        return new RedirectView("/account/cart?remove");
    }

    @GetMapping("/checkout")
    public String getCheckoutPage(Model model, @SessionAttribute User user) {
        user = userRepository.findById(user.getUserId()).get();
        Set<Listing> listings = user.getCart().getListings();

        // Add subtotal
        int subtotalCents = 0;
        for (Listing listing : listings) {
            subtotalCents += listing.getPriceInCents();
        }
        model.addAttribute("subtotal", (double)(subtotalCents) / 100);

        // Add cards and addresses
        model.addAttribute("cards", user.getCreditCards());
        model.addAttribute("addresses", user.getAddresses());

        return "checkout";
    }

    @PostMapping("/checkout")
    public RedirectView doCheckout(@SessionAttribute User user) {
        user = userRepository.findById(user.getUserId()).get();

        // Mark listings as inactive if the last one is bought
        for (Listing listing : user.getCart().getListings()) {
            if (listing.getCarts().size() == 1) {
                listing.setOpen(false);
            }
            listing.getCarts().remove(user.getCart());
            listingRepository.save(listing);
        }

        // Clear the user's cart
        user.getCart().getListings().clear();
        userRepository.save(user);

        return new RedirectView("/");
    }

}
