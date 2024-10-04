package com.k_plus.internship.ViewControllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String sighUp() {
        return "signup";
    }

    //NEED A TESTING
    @PreAuthorize("hasAnyRole(\"ADMIN\", \"USER\")")
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
