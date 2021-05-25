package me.ronygomes.identity_fort.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    private static final String VIEW_NAME = "login";

    @GetMapping("/login")
    public String showLoginPage() {
        return VIEW_NAME;
    }
}
