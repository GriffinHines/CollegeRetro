package com.retro.collegeretro.Controller;

import com.retro.collegeretro.Model.Listing;
import com.retro.collegeretro.Repository.UserRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserRepository userRepository;

    @Value("${retro.scrape}")
    private boolean shouldScrape;

    /**
     * Deletes previously generated users, creates five users with an address
     * and a credit card, and for each user, scrapes 10 listings from ebay.
     * @return Redirects to homepage.
     */
    @GetMapping("/scrape")
    public RedirectView generateUsersAndScrapeListings() {
        // Do nothing if shouldn't scrape (Set by properties)
        if (!shouldScrape) {
            return new RedirectView("/");
        }

        // Remove all users that this method might have generated in the past
        // TODO
        // ex. userRepository.delete(userRepository.findByUsername("BigYoshi123"));
        //     if you make a user named BigYoshi123

        // Generate five users with credit cards
        // TODO
        // Create a user object, create a credit card, create an address, set the
        // user's address and credit card, and userRepository.save(theUser). 5 times.

        // For each user, scrape 10 listings from eBay
        // TODO
        // Use a college related search term for each user, like textbook or fridge.

        return new RedirectView("/");
    }

    /**
     * Scrapes some listings from eBay given a search term.
     * @param n number of listings to scrape
     * @param searchQuery search term for eBay
     * @return list of listings generated from eBay
     * @throws IOException if something messes up with Jsoup
     */
    private List<Listing> scrapeNListings(int n, String searchQuery) throws IOException {
        // Generate URL
        String url = "https://www.ebay.com/sch/i.html?_nkw=" + searchQuery;

        // Get page
        Document document = Jsoup.connect(url).get();

        // Return object
        List<Listing> listings = new ArrayList<>();

        // TODO the rest is up to you!

        return listings;
    }
}
