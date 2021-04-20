package com.retro.collegeretro.Controller;

import com.retro.collegeretro.Model.Address;
import com.retro.collegeretro.Model.CreditCard;
import com.retro.collegeretro.Model.Listing;
import com.retro.collegeretro.Model.User;
import com.retro.collegeretro.Repository.ListingRepository;
import com.retro.collegeretro.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.transaction.Transactional;
import java.util.*;

import java.io.IOException;

import static java.lang.Integer.parseInt;

@Controller
@Slf4j
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ListingRepository listingRepository;

    @Value("${retro.scrape}")
    private boolean shouldScrape;

    @GetMapping("/listings")
    public String listings(Model model) {
        List<Listing> listings = listingRepository.findAll();
        model.addAttribute("listings", listings);
        return "listings";
    }

    @PostMapping("/listing/markStatus")
    @ResponseStatus(value = HttpStatus.OK)
    public void markStatus(@RequestParam Long listingId, @RequestParam Boolean isOpen) {
        Listing listing = listingRepository.findById(listingId).get();
        listing.setOpen(isOpen);
        listingRepository.save(listing);
    }

    /**
     * Deletes previously generated users, creates five users with an address
     * and a credit card, and for each user, scrapes 10 listings from ebay.
     *
     * @return Redirects to homepage.
     */
    @GetMapping("/scrape")
    @Transactional
    public RedirectView generateUsersAndScrapeListings() throws IOException {
        // Do nothing if shouldn't scrape (Set by properties)
        if (!shouldScrape) {
            return new RedirectView("/");
        }

        // Remove all users that this method might have generated in the past
        // ex. userRepository.delete(userRepository.findByUsername("BigYoshi123"));
        //     if you make a user named BigYoshi123
        userRepository.deleteByUsername("Bob");
        userRepository.deleteByUsername("Alice");
        userRepository.deleteByUsername("Steve");
        userRepository.deleteByUsername("Stacy");
        userRepository.deleteByUsername("Molly");

        // Generate five users with credit cards
        //Generate 5 credit cards
        String cardNumber = "";
        Random random = new Random();
        int randomInt;
        Set<CreditCard> creditCards;
        User[] user = new User[5];
        for (int i = 0; i < user.length; i++) {
            user[i] = new User();
        }
        user[0].setUsername("Bob");
        user[1].setUsername("Alice");
        user[2].setUsername("Steve");
        user[3].setUsername("Stacy");
        user[3].setUsername("Molly");
        for (int k = 1; k <= 5; k++) {
            creditCards = new HashSet<>();
            for (int i = 0; i < 15; i++) {
                randomInt = random.nextInt(10);
                cardNumber += randomInt;
            }
            creditCards.add(new CreditCard("user" + k, cardNumber, random.nextInt(13), parseInt("202" + random.nextInt(10)), random.nextInt(1000), user[k - 1]));
            user[k - 1].setCreditCards(creditCards);
        }
        //Generate 5 addresses
        Set<Address> addresses = new HashSet<>();
        addresses.add(new Address("P Sherman Wallaby Way", null, "2021", "Syndey", "New South Wales", user[0]));
        user[0].setAddresses(addresses);
        addresses = new HashSet<>();
        addresses.add(new Address("Mordor", null, "30338", "Mordor", "Mordor", user[1]));
        user[1].setAddresses(addresses);
        addresses = new HashSet<>();
        addresses.add(new Address("Townsville", null, "10128", "Townsville", "New York", user[2]));
        user[2].setAddresses(addresses);
        addresses = new HashSet<>();
        addresses.add(new Address("Dooblerville", null, "60771", "Doubleston", "Dorglerbrook", user[3]));
        user[3].setAddresses(addresses);
        addresses = new HashSet<>();
        addresses.add(new Address("Candy Island", null, "60601", "Candy Island", "Candy Island", user[4]));
        user[4].setAddresses(addresses);

        userRepository.save(user[0]);
        userRepository.save(user[1]);
        userRepository.save(user[2]);
        userRepository.save(user[3]);
        userRepository.save(user[4]);

        // For each user, scrape 10 listings from eBay
        // Use a college related search term for each user, like textbook or fridge.
        List<Listing> bookListings = scrapeListings(10, "textbooks");
        user[0].setListings(bookListings);
        bookListings.forEach((listing) -> listing.setUser(user[0]));
        userRepository.save(user[0]);
        log.info("Scraped textbooks");

        List<Listing> furnitureListings = scrapeListings(10, "furniture");
        user[1].setListings(furnitureListings);
        furnitureListings.forEach((listing) -> listing.setUser(user[1]));
        userRepository.save(user[1]);
        log.info("Scraped furniture");

        List<Listing> appliancesListings = scrapeListings(10, "appliances");
        user[2].setListings(appliancesListings);
        appliancesListings.forEach((listing) -> listing.setUser(user[2]));
        userRepository.save(user[2]);
        log.info("Scraped appliances");

        List<Listing> computingListings = scrapeListings(10, "computing");
        user[3].setListings(computingListings);
        computingListings.forEach((listing) -> listing.setUser(user[3]));
        userRepository.save(user[3]);
        log.info("Scraped computing");

        log.info("Done scraping");
        return new RedirectView("/");
    }

    /**
     * Scrapes some listings from eBay given a search term.
     *
     * @param maxItems    maximum number of listings to scrape
     * @param searchQuery search term for eBay
     * @return list of listings generated from eBay
     * @throws IOException if something messes up with Jsoup
     */
    private List<Listing> scrapeListings(int maxItems, String searchQuery) throws IOException {
        List<Listing> listings = new ArrayList<>();
        String url = "https://www.ebay.com/sch/i.html?_nkw=" + searchQuery + "&rt=nc&LH_BIN=1";

        Document document = Jsoup.connect(url).get();
        Element centerList = document.getElementById("mainContent");
        Elements individualItems = centerList.getElementsByClass("s-item__link");

        //Follow links to item pages
        int itemsScraped = 0;
        for (Element link : individualItems) {
            if (itemsScraped >= maxItems) {
                break;
            } // if
            String linkHref = link.attr("href");
            Document subDoc = Jsoup.connect(linkHref).get();

            //Set listing parameters according to ebay item's scraped data
            Listing listing = new Listing();
            //Category (ignore ebay's version and just use the search term)
            listing.setCategory(searchQuery);
            //ListingName
            Element titleElement = subDoc.getElementById("itemTitle");
            String title = titleElement.text().substring(14);
            listing.setListingName(title);
            //Price
            Element priceElement = subDoc.getElementById("prcIsum");
            if (priceElement == null)
                continue;
            String priceText = priceElement.text();
            double price = 0.0;
            try {
                price = Double.parseDouble(priceText.substring(priceText.indexOf('$') + 1));
            } catch (Exception e) {
                continue;
            } // try
            int cents = (int) (price * 100);
            listing.setPriceInCents(cents);
            //Is Open
            listing.setOpen(true); // default to `true`
            //Description
//            Element descriptionElement = subDoc.getElementById("desc_ifr");
//            String description = descriptionElement.text();
            listing.setDescription("");
            //Quantity, couldn't find this listed on ebay
            listing.setQuantity(1);
            //Image
            Element imageElement = subDoc.getElementById("icImg");
            listing.setImageURL(imageElement.attr("src"));

            //Save listing
            listings.add(listing);
            itemsScraped++;
        }

        return listings;
    }
}
