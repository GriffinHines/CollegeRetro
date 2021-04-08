package com.retro.collegeretro;

import com.retro.collegeretro.Model.Listing;
import com.retro.collegeretro.Repository.ListingRepository;
import junit.framework.Assert;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ScrapingTests {

    @Autowired
    private ListingRepository listingRepository;

    @Test
    public void scrapeNListings() throws IOException {

        // Generate URL
        String searchQuery = "Tchotchke";
        String url = "https://www.ebay.com/sch/i.html?_nkw=" + searchQuery;

        // Get page
        Document document = Jsoup.connect(url).get();
        //Select ebay items
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
            for (Element item : categories) {
                category += item.text() + "/";
            }
            listing.setCategory(category);
            //ListingName
            Element titleElement = subDoc.getElementById("itemTitle");
            String title = titleElement.text().substring(14);
            listing.setListingName(title);
            //Price
            Element priceElement = subDoc.getElementById("prcIsum");
            if (priceElement == null)
                priceElement = subDoc.getElementById("prcIsum_bidPrice");
            //String price = priceElement.text();
            // TODO convert price to int
            listing.setPriceInCents(1);
            //Is Open
            // TODO is this necessary? I can't find any closed auctions to check against

            //Description
            Element descriptionElement = subDoc.getElementById("desc_ifr");
            String description = descriptionElement.text();
            listing.setDescription(description);
            //Quantity, couldn't find this listed on ebay
            listing.setQuantity(1);
            //Image
            Element imageElement = subDoc.getElementById("icImg");
            //Open a URL Stream
            Connection.Response resultImageResponse = Jsoup.connect(imageElement.attr("src")).ignoreContentType(true).execute();

            // output here
            FileOutputStream out = (new FileOutputStream(new java.io.File("src/main/resources/images/image_" + id +".jpg")));
            out.write(resultImageResponse.bodyAsBytes());  // resultImageResponse.body() is where the image's contents are.
            out.close();
            //listing = listingRepository.save(listing);
        }


        // Return object
        List<Listing> listings = new ArrayList<>();

        // TODO the rest is up to you!
    }
}
