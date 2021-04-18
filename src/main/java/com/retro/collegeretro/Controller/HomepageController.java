package com.retro.collegeretro.Controller;

import com.retro.collegeretro.Model.Listing;
import com.retro.collegeretro.Model.User;
import com.retro.collegeretro.Repository.ListingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomepageController {

    private final ListingRepository listingRepository;

    public HomepageController(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    @GetMapping("/")
    public String getHomepage(@SessionAttribute(required = false) User user, Model model) {
        Page<Listing> textbooks = listingRepository.findAllByCategory(PageRequest.of(0, 5), "textbooks");
        Page<Listing> furniture = listingRepository.findAllByCategory(PageRequest.of(0, 5), "furniture");
        Page<Listing> appliances = listingRepository.findAllByCategory(PageRequest.of(0, 5), "appliances");
        Page<Listing> computing = listingRepository.findAllByCategory(PageRequest.of(0, 5), "computing");

        model.addAttribute("textbooks", textbooks);
        model.addAttribute("furniture", furniture);
        model.addAttribute("appliances", appliances);
        model.addAttribute("computing", computing);

        return "index";
    }

    @GetMapping("/home")
    public RedirectView getHomepage2() {
        return new RedirectView("/");
    }

}
