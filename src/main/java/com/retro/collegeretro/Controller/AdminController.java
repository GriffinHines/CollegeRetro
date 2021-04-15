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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.transaction.Transactional;
import java.util.*;

import java.io.IOException;

import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

@Controller
@Slf4j
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
        // Create a user object, create a credit card, create an address, set the
        // user's address and credit card, and userRepository.save(theUser). 5 times.
        userRepository.save(user[0]);
        userRepository.save(user[1]);
        userRepository.save(user[2]);
        userRepository.save(user[3]);
        userRepository.save(user[4]);
        // For each user, scrape 10 listings from eBay
        // Use a college related search term for each user, like textbook or fridge.
        List<Listing> bookListings = scrapeNListings(10, "book");
        user[0].setListings(bookListings);
        bookListings.forEach((listing) -> listing.setUser(user[0]));
        userRepository.save(user[0]);
        log.info("Done scraping");

        return new RedirectView("/");
    }

    /**
     * Scrapes some listings from eBay given a search term.
     *
     * @param n           number of listings to scrape
     * @param searchQuery search term for eBay
     * @return list of listings generated from eBay
     * @throws IOException if something messes up with Jsoup
     */
    private List<Listing> scrapeNListings(int n, String searchQuery) throws IOException {
        List<Listing> listings = new ArrayList<>();
        String url = "https://www.ebay.com/sch/i.html?_nkw=" + searchQuery + "&rt=nc&LH_BIN=1";

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
            try {
                // Just the most basic category
                listing.setCategory(categories.get(0).text());
            } catch (IndexOutOfBoundsException ioobe) {
                continue;
            } // try
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
            Element descriptionElement = subDoc.getElementById("desc_ifr");
            String description = descriptionElement.text();
            listing.setDescription(description);
            //Quantity, couldn't find this listed on ebay
            listing.setQuantity(1);
            //Image
            Element imageElement = subDoc.getElementById("icImg");
            listing.setImageURL(imageElement.attr("src"));
//            Connection.Response resultImageResponse = Jsoup.connect(imageElement.attr("src")).ignoreContentType(true).execute();
//            FileOutputStream out = (new FileOutputStream( id + ".jpg"));
//            out.write(resultImageResponse.bodyAsBytes());  // resultImageResponse.body() is where the image's contents are.
//            out.close();

            //Save listing
            listings.add(listing);
        }

        return listings;
    }
}
