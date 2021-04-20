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
    public String getListings(Model model,
                              @RequestParam(required = false) String query,
                              @RequestParam(required = false) String cat,
                              @RequestParam(required = false, defaultValue = "0") Integer page,
                              @RequestParam(required = false, defaultValue = "20") Integer size) {
        if (cat != null) {
            Page<Listing> allByCategory = listingRepository.findAllByCategoryAndIsOpenTrue(PageRequest.of(page, size), cat);
            model.addAttribute("listings", allByCategory);
        } else {
            Page<Listing> allByQuery = listingRepository.findAllByListingNameLikeAndIsOpenTrue(PageRequest.of(page, size), "%" + query + "%");
            model.addAttribute("listings", allByQuery);
        }
        return "search";
    } //getListings

} // SearchController
