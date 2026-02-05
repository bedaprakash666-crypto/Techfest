package com.eflair.techfeast.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/technical")
    public String technicalPage() {
        return "technical";  // will load technical.html
    }

    @GetMapping("/nontechnical")
    public String nonTechnicalPage() {
        return "nontechnical";  // will load nontechnical.html
    }
}
