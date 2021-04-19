package com.retro.collegeretro.Controller;

import com.retro.collegeretro.Model.Listing;
import com.retro.collegeretro.Repository.ListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/listing")
public class ListingController {

    @Autowired
    private ListingRepository listingRepository;

    @GetMapping("/{listingId}")
    public String getListing(@PathVariable Long listingId, Model model) {
        final Optional<Listing> byId = listingRepository.findById(listingId);
        if (byId.isPresent()) {
            Listing listing = byId.get();
            model.addAttribute("listing", listing);
            return "listing";
        } else {
            return "redirect:/listing/404";
        }
    }

    @GetMapping("/404")
    public String getListingNotFound() {
        return "listing-error";
    }

}
