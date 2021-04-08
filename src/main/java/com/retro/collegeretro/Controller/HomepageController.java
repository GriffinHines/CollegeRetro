package com.retro.collegeretro.Controller;

import com.retro.collegeretro.Model.Listing;
import com.retro.collegeretro.Model.User;
import com.retro.collegeretro.Repository.ListingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
public class HomepageController {

    private final ListingRepository listingRepository;

    public HomepageController(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    @GetMapping("/")
    public String getHomepage(@SessionAttribute(required = false) User user, Model model) {
        // TODO(michaelrehman): filter to just a few ones
        List<Listing> listings = listingRepository.findAll();

        // Sample (not saved to database)
        Listing listing = new Listing();
        listing.setListingName("name");
        listing.setCategory("cate");
        listing.setDescription("some desc");
        listing.setOpen(true);
        listing.setPriceInCents(123);
        listing.setQuantity(12);
        listings.add(listing);
        model.addAttribute("listings", listings);
        return "index";
    }

    @GetMapping("/home")
    public RedirectView getHomepage2() {
        return new RedirectView("/");
    }

}
