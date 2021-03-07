package com.retro.collegeretro.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class HomepageController {

    @GetMapping("/")
    public String getHomepage() {
        return "index";
    }

    @GetMapping("/home")
    public RedirectView getHomepage2() {
        return new RedirectView("/");
    }

}
