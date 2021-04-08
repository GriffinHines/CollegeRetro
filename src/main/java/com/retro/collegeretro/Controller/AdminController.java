package com.retro.collegeretro.Controller;

import com.retro.collegeretro.Model.Address;
import com.retro.collegeretro.Model.CreditCard;
import com.retro.collegeretro.Model.Listing;
import com.retro.collegeretro.Model.User;
import com.retro.collegeretro.Repository.ListingRepository;
import com.retro.collegeretro.Repository.UserRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.*;

import java.io.IOException;

import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserRepository userRepository;
    private ListingRepository listingRepository;

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
        // ex. userRepository.delete(userRepository.findByUsername("BigYoshi123"));
        //     if you make a user named BigYoshi123
        userRepository.delete(userRepository.findByUsername("Bob"));
        userRepository.delete(userRepository.findByUsername("Alice"));
        userRepository.delete(userRepository.findByUsername("Steve"));
        userRepository.delete(userRepository.findByUsername("Stacy"));
        userRepository.delete(userRepository.findByUsername("Molly"));

        // Generate five users with credit cards
        //Generate 5 credit cards
        String cardNumber = "";
        Random random = new Random();
        int randomInt;
        Set<CreditCard> creditCards;
        User[] user = new User[5];
        user[0].setUsername("Bob");
        user[1].setUsername("Alice");
        user[2].setUsername("Steve");
        user[3].setUsername("Stacy");
        user[3].setUsername("Molly");
        for(int k=1; k<=5; k++) {
            creditCards = new HashSet<>();
            for (int i=0; i<15; i++) {
                randomInt = random.nextInt(10);
                cardNumber += randomInt;
            }
            creditCards.add(new CreditCard("user"+k, cardNumber, random.nextInt(13), parseInt("202"+valueOf(random.nextInt(10))), random.nextInt(1000), user[k-1]));
            user[k-1].setCreditCards(creditCards);
        }
        //Generate 5 addresses
        Set<Address> addresses = new HashSet<>();
        addresses.add(new Address("P Sherman Wallaby Way", "2021", "Syndey", "New South Wales", user[0]));
        user[0].setAddresses(addresses);
        addresses = new HashSet<>();
        addresses.add(new Address("Mordor", "30338", "Mordor", "Mordor", user[1]));
        user[1].setAddresses(addresses);
        addresses = new HashSet<>();
        addresses.add(new Address("Townsville", "10128", "Townsville", "New York", user[2]));
        user[2].setAddresses(addresses);
        addresses = new HashSet<>();
        addresses.add(new Address("Dooblerville", "60771", "Doubleston", "Dorglerbrook", user[3]));
        user[3].setAddresses(addresses);
        addresses = new HashSet<>();
        addresses.add(new Address("Candy Island", "60601", "Candy Island", "Candy Island", user[4]));
        user[4].setAddresses(addresses);
        // Create a user object, create a credit card, create an address, set the
        // user's address and credit card, and userRepository.save(theUser). 5 times.
        userRepository.save(user[0]);
        userRepository.save(user[1]);
        userRepository.save(user[2]);
        userRepository.save(user[3]);
        userRepository.save(user[4]);
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
        String url = "https://www.ebay.com/sch/i.html?_nkw=" + searchQuery;

        Document document = Jsoup.connect(url).get();
        Element centerList = document.getElementById("mainContent");
        Elements individualItems = centerList.getElementsByClass("s-item__link");

        //Follow links to item pages
        int id = 0;
        for (Element link : individualItems) {
            id++;
            String linkHref = link.attr("href");
            Document subDoc = Jsoup.connect(linkHref).get();

            //Set listing parameters according to ebay item's scraped data
            Listing listing = new Listing();
            //ID
            listing.setListingId(id);
            //Category
            Element categoryHeader = subDoc.getElementById("vi-VR-brumb-lnkLst");
            Elements categories = categoryHeader.getElementsByAttributeValue("itemprop", "name");
            String category = "";
            for(Element item : categories) {
                category+=item.text() + "/";
            }
            listing.setCategory(category);
            //ListingName
            Element titleElement = subDoc.getElementById("itemTitle");
            String title = titleElement.text().substring(14);
            listing.setListingName(title);
            //Price
            Element priceElement = subDoc.getElementById("prcIsum");
            if(priceElement == null)
                priceElement = subDoc.getElementById("prcIsum_bidPrice");
            String price = priceElement.text();
                // TODO convert price to int
            //Is Open
                // TODO is this necessary? I can't find any closed auctions to check against
            //Description
            Element descriptionElement = subDoc.getElementById("desc_ifr");
            String description = descriptionElement.text();
            listing.setDescription(description);
            //Quantity, couldn't find this listed on ebay
            listing.setQuantity(1);

            //Save listing
            listing = listingRepository.save(listing);
        }

        // Return object
        List<Listing> listings = new ArrayList<>();

        return listings;
    }
}
