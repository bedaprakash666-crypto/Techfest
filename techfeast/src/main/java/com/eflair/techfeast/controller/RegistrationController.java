package com.eflair.techfeast.controller;

import com.eflair.techfeast.model.Registration;
import com.eflair.techfeast.repository.RegistrationRepository;
import com.eflair.techfeast.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

import java.util.List;

@Controller
@RequestMapping("/api/registrations")
public class RegistrationController {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    // ✅ STEP 1: LOAD REGISTRATION PAGE (GET)
    @GetMapping("/form")
    public String showRegistrationForm(@RequestParam(value = "event", required = false) String event,Model model) {

        List<String> events = List.of(
                "Hackathon",
                "Project Expo",
                "Ideathon",
                "IoT / Smart Circuit Design Championship",
                "Coding Level 1.0",
                "Technical Poster Presentation",
                "Tech Quiz",
                "J30M — Presentation Challenge",
                "Robo Race",
                "Virtual Campus Drive",
                "Hardware Redesign",
                "Free Fire",
                "Rangoli Competition",
                "Digital Painting Competition",
                "Treasure Hunt",
                "Reels Challenge",
                "Chess Championship",
                "Tug of War"

        );

        model.addAttribute("events", events);
        model.addAttribute("selectedEvent", event);

        return "registration";
    }

    // ✅ STEP 2: SAVE REGISTRATION (POST)
    @PostMapping
    public String saveRegistration(
            @ModelAttribute Registration registration,
            @RequestParam(value = "selectedEvents", required = false) String selectedEvents,
            @RequestParam(value = "paymentScreenshot", required = false) MultipartFile screenshot,
            Model model) {

        // PAYMENT
        if ("ONLINE".equalsIgnoreCase(registration.getPaymentMode())) {

            if (screenshot == null || screenshot.isEmpty()) {
                model.addAttribute("error", "Payment screenshot required");
                return "registration";
            }

            String imageUrl = cloudinaryService.uploadImage(screenshot);
            registration.setScreenshotUrl(imageUrl);

        } else {
            registration.setScreenshotUrl("CASH");
        }

        String eventNames = (selectedEvents != null && !selectedEvents.isBlank())
                ? selectedEvents
                : registration.getEventName();

        List<Long> savedIds = new ArrayList<>();

        if (eventNames != null && !eventNames.isBlank()) {

            String[] events = eventNames.split(",");

            for (String event : events) {

                if (event.trim().isEmpty()) continue;

                Registration newReg = new Registration();

                newReg.setStudentName(registration.getStudentName());
                newReg.setRollNo(registration.getRollNo());
                newReg.setDepartment(registration.getDepartment());
                newReg.setYear(registration.getYear());
                newReg.setPhone(registration.getPhone());
                newReg.setEmail(registration.getEmail());
                newReg.setTransactionId(registration.getTransactionId());
                newReg.setPaymentMode(registration.getPaymentMode());
                newReg.setScreenshotUrl(registration.getScreenshotUrl());
                newReg.setCashReceiver(registration.getCashReceiver());
                newReg.setReceiverPhone(registration.getReceiverPhone());
                newReg.setEventName(event.trim());

                registration.setTransactionId("CASH");

                Registration saved = registrationRepository.save(newReg);
                savedIds.add(saved.getId());
            }
        }

        String ids = savedIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        return "redirect:/api/registrations/success?ids=" + ids;
    }
    // ✅ SUCCESS PAGE
    @GetMapping("/success")
    public String showSuccessPage(@RequestParam("ids") String ids, Model model) {

        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();

        List<Registration> registrations = registrationRepository.findAllById(idList);

        model.addAttribute("registrations", registrations);

        return "success";
    }


    // ✅ OPTIONAL: VIEW ALL REGISTRATIONS (API)
    @GetMapping
    @ResponseBody
    public List<Registration> getAllRegistrations() {
        return registrationRepository.findAll();
    }
}
