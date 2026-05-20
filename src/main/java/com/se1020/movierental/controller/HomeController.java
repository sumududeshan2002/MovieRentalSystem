package com.se1020.movierental.controller;

import com.se1020.movierental.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/home")
    public String home() {
        return "index";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/users/login";
        }
        return "admin/dashboard";
    }

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        return user != null && "ADMIN".equals(user.getRole());
    }
}


