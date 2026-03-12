package com.eflair.techfeast.controller;

import com.eflair.techfeast.model.Registration;
import com.eflair.techfeast.repository.RegistrationRepository;
import com.eflair.techfeast.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public String showRegistrationForm(Model model) {

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
        return "registration";
    }

    // ✅ STEP 2: SAVE REGISTRATION (POST)
    @PostMapping
    public String saveRegistration(
            @ModelAttribute Registration registration,
            @RequestParam(value = "paymentScreenshot", required = false) MultipartFile screenshot,
            Model model) {

        // 🔹 If payment mode is ONLINE → upload screenshot
        if ("online".equalsIgnoreCase(registration.getPaymentMode())) {

            if (screenshot == null || screenshot.isEmpty()) {
                model.addAttribute("error", "Payment screenshot is required for online payment");
                return "registration";
            }

            // Upload to Cloudinary
            String imageUrl = cloudinaryService.uploadImage(screenshot);
            registration.setScreenshotUrl(imageUrl);

        } else {
            // 🔹 CASH payment
            registration.setScreenshotUrl("CASH");
        }

        // Save registration
        registrationRepository.save(registration);

        // 🔹 Redirect instead of returning directly (VERY IMPORTANT)
        return "redirect:/api/registrations/success?id=" + registration.getId();
    }

    // ✅ SUCCESS PAGE
    @GetMapping("/success")
    public String showSuccessPage(@RequestParam("id") Long id, Model model) {

        Registration registration = registrationRepository.findById(id).orElse(null);

        model.addAttribute("registration", registration);
        return "success";   // success.html
    }


    // ✅ OPTIONAL: VIEW ALL REGISTRATIONS (API)
    @GetMapping
    @ResponseBody
    public List<Registration> getAllRegistrations() {
        return registrationRepository.findAll();
    }
}
