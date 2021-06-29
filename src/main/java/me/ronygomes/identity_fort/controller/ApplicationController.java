package me.ronygomes.identity_fort.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ApplicationController {

    private static final String LIST_VIEW_NAME = "applicationList";

    @GetMapping(value = "/applicationList")
    public String viewApplications() {
        return LIST_VIEW_NAME;
    }

    @GetMapping(value = "/application")
    public String showApplication(@RequestParam(defaultValue = "0") int id) {
        return LIST_VIEW_NAME;
    }
}
