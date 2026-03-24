package com.eflair.techfeast.controller;

import com.eflair.techfeast.model.Registration;
import com.eflair.techfeast.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class RegistrationPageController {

    @Autowired
    private RegistrationRepository registrationRepository;

    @GetMapping("/register")
    public String showRegistrationPage(
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) String eventName,
            Model model) {

        List<String> technicalEvents = new ArrayList<>(List.of(
                "Hackathon",
                "Project Expo",
                "Ideathon",
                "IoT / Smart Circuit Design Championship",
                "Coding Competition",
                "Technical Poster Presentation",
                "Tech Quiz",
                "J30M — Presentation Challenge",
                "Robo War",
                "Virtual Campus Drive",
                "Hardware Redesign",
                "Mind Maze"
        ));

        List<String> nonTechnicalEvents = new ArrayList<>(Arrays.asList(
                "Free Fire",
                "BGMI",
                "Rangoli Competition",
                "Digital Painting Competition",
                "Treasure Hunt",
                "Reels Challenge",
//                "Chess Championship",
                "Tug of War",
                "RC24"

        ));
//        List<String> allEvents = new ArrayList<>();
//        allEvents.addAll(technicalEvents);
//        allEvents.addAll(nonTechnicalEvents);

        if (eventName != null) {
            model.addAttribute("events", List.of(eventName));
            model.addAttribute("selectedEvent", eventName);

        } else if ("technical".equalsIgnoreCase(eventType)) {
            model.addAttribute("events", technicalEvents);

        } else if ("nontechnical".equalsIgnoreCase(eventType)) {
            model.addAttribute("events", nonTechnicalEvents);

        }
//        // Case 4: ALL events (Home page register)
//        else {
//            model.addAttribute("events", allEvents);
//        }
        else {
            technicalEvents.addAll(nonTechnicalEvents);
            model.addAttribute("events", technicalEvents);
        }

        return "registration"; // templates/registration.html
    }

    // POST handler to save registration and show success page
//    @PostMapping("/register")
//    public String saveRegistration(@ModelAttribute Registration registration, Model model) {
//        registrationRepository.save(registration);
//        model.addAttribute("registration", registration);
//        return "success"; // templates/success.html
//    }
    @PostMapping("/register")
    public String saveRegistration(@ModelAttribute Registration registration, Model model) {

        // ✅ FIX: remove leading comma if present
        String eventNames = registration.getEventName();
        if (eventNames != null) {
            eventNames = eventNames.replaceAll("^,+", "");
            registration.setEventName(eventNames);
        }


        registrationRepository.save(registration);
        model.addAttribute("registration", registration);
        return "success";
    }

}
