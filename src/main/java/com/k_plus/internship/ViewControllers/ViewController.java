package com.k_plus.internship.ViewControllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @GetMapping("/courses/{id}")
    public String courseDetails(@PathVariable UUID id, Model model) {
        return "courses-details";
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

    @GetMapping("/report-issue")
    public String reportIssue() {
        return "report-form";
    }

    @GetMapping("/courses/{courseId}/articles/{articleId}")
    public String articleDetails(@PathVariable UUID courseId, @PathVariable UUID articleId, Model model) {
        return "articles-details";
    }

    @GetMapping("/courses/{courseId}/testings/{testId}")
    public String testingDetails(@PathVariable UUID courseId, @PathVariable UUID testId, Model model) {
        return "testing-details";
    }

    @GetMapping("/agreement")
    public String agreement() {
        return "UserAgreement";
    }
}
