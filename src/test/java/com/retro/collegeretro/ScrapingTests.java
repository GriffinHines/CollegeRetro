package com.retro.collegeretro;

import com.retro.collegeretro.Model.Listing;
import junit.framework.Assert;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ScrapingTests {

    @Test
    public void scrapeNListings(int n, String searchQuery) throws IOException {
        // Generate URL
        System.out.println("1hello1");
        /*
        searchQuery = "textbooks";
        String url = "https://www.ebay.com/sch/i.html?_nkw=" + searchQuery;

        // Get page
        Document document = Jsoup.connect(url).get();
        Elements links = document.select("ul.srp-results srp-list clearfix");
        System.out.println("hello");
        System.out.println(links);


        // Return object
        List<Listing> listings = new ArrayList<>();
        */
        // TODO the rest is up to you!
    }
}
