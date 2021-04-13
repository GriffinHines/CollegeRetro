package com.retro.collegeretro.Controller;

import com.retro.collegeretro.Model.Listing;
import com.retro.collegeretro.Repository.ListingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class SearchController {

    private final ListingRepository listingRepository;

    public SearchController(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    } // SearchController

    @GetMapping("/search")
    public String getListings(Model model, @RequestParam String cat, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        if (page == null) {
            page = 0;
        } // if
        if (size == null) {
            size = 20;
        } // if
        Page<Listing> allByCategory = listingRepository.findAllByCategory(PageRequest.of(page, size), cat);
        model.addAttribute("listings", allByCategory);
        return "search";
    } //getListings

} // SearchController
