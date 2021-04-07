package com.retro.collegeretro.Controller;

import com.retro.collegeretro.Model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class HomepageController {

    @GetMapping("/")
    public String getHomepage(@SessionAttribute(required = false) User user) {
        return "index";
    }

    @GetMapping("/home")
    public RedirectView getHomepage2() {
        return new RedirectView("/");
    }

}
