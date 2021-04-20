package com.retro.collegeretro.Controller;

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

import java.util.Optional;

@Controller
@RequestMapping("/listing")
public class ListingController {

    @Autowired
    private ListingRepository listingRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{listingId}")
    public String getListing(@PathVariable Long listingId, Model model, @SessionAttribute(required = false) User user) {
        final Optional<Listing> byId = listingRepository.findById(listingId);
        if (byId.isPresent()) {
            // Get listing
            Listing listing = byId.get();
            model.addAttribute("listing", listing);

            // Check if already in cart.
            model.addAttribute("inCart", false);
            if (user != null) {
                user = userRepository.findById(user.getUserId()).get();
                if (user.getCart().getListings().contains(listing)) {
                    model.addAttribute("inCart", true);
                }
            }

            return "listing";
        } else {
            return "redirect:/listing/404";
        }
    }

    @GetMapping("/404")
    public String getListingNotFound() {
        return "listing-error";
    }

    @GetMapping("/add/{listingId}")
    public RedirectView addToCart(@PathVariable Long listingId, @SessionAttribute User user) {
        user = userRepository.findById(user.getUserId()).get();
        user.getCart().getListings().add(listingRepository.findById(listingId).get());
        userRepository.save(user);
        return new RedirectView("/listing/" + listingId + "?cart");
    }

}
