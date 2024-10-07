package com.k_plus.internship.ViewControllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

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

    //@PreAuthorize("hasRole(\"ADMIN\")")
    @PreAuthorize("hasRole(\"ADMIN\")")
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/courses")
    public String courses() {
        return "courses";
    }

    @GetMapping("/course/{id}")
    public String courseDetails(@PathVariable UUID id) {
        return "course-details";
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }
}
