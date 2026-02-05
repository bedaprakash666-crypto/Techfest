package com.eflair.techfeast.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Value;


@Controller
public class AdminAuthController {

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @GetMapping("/admin/login")
    public String showLoginPage() {
        return "admin-login";
    }

    @PostMapping("/admin/login")
    public String handleLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        if (adminUsername.equals(username) && adminPassword.equals(password)) {

            session.setAttribute("ADMIN_LOGGED_IN", true);
//            return "redirect:/admin/registrations";
            return "redirect:/admin/dashboard";
        }

        model.addAttribute("error", true);
        return "admin-login";
    }

    @GetMapping("/admin/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
}
