package com.eflair.techfeast.controller;

import com.eflair.techfeast.model.Registration;
import com.eflair.techfeast.repository.RegistrationRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import jakarta.servlet.ServletOutputStream;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;


import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private RegistrationRepository registrationRepository;

    @GetMapping("/admin/registrations")
    public String viewAllRegistrations(
            HttpSession session,
            HttpServletResponse response,   // ✅ ADDED
            @RequestParam(required = false) String event,
            @RequestParam(required = false) String payment,
            Model model) {

        // 🔐 STEP 4: BLOCK BROWSER CACHE (VERY IMPORTANT)
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        // 🔒 SESSION CHECK
        Boolean loggedIn = (Boolean) session.getAttribute("ADMIN_LOGGED_IN");
        if (loggedIn == null || !loggedIn) {
            return "redirect:/admin/login";
        }

        List<Registration> registrations;

        if (event != null && !event.isEmpty()
                && payment != null && !payment.isEmpty()) {

            registrations =
                    registrationRepository.findByEventNameAndPaymentMode(event, payment);

        } else if (event != null && !event.isEmpty()) {

            registrations = registrationRepository.findByEventName(event);

        } else if (payment != null && !payment.isEmpty()) {

            registrations = registrationRepository.findByPaymentMode(payment);

        } else {
            registrations = registrationRepository.findAll();
        }

        // 🔥 GET ALL EVENTS (for filter dropdown)
        List<String> allEvents = registrationRepository
                .findAll()
                .stream()
                .map(Registration::getEventName)
                .distinct()
                .toList();

        model.addAttribute("registrations", registrations);
        model.addAttribute("events", allEvents);
        model.addAttribute("selectedEvent", event);
        model.addAttribute("selectedPayment", payment);

        return "admin-registrations";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(
            HttpSession session,
            HttpServletResponse response,
            Model model) {

        // 🔐 Block cache
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        // 🔒 Session check
        Boolean loggedIn = (Boolean) session.getAttribute("ADMIN_LOGGED_IN");
        if (loggedIn == null || !loggedIn) {
            return "redirect:/admin/login";
        }

//        List<String> allEvents = registrationRepository
//                .findAll()
//                .stream()
//                .map(Registration::getEventName)
//                .distinct()
//                .toList();
//
//        model.addAttribute("events", allEvents);
        List<String> eventNames = registrationRepository
                .findAll()
                .stream()
                .map(Registration::getEventName)
                .filter(e -> e != null && !e.isBlank())
                .distinct()
                .sorted()
                .toList();

        model.addAttribute("events", eventNames);

        // 📊 Group and count registrations by event name
        // 📊 Group and count registrations by event name
        // Try changing .getEventName() to .getEvent() if 'eventName' doesn't work
//        List<Registration> all = null;
//        Map<String, Long> eventStats = all.stream()
//                .filter(r -> r != null && r.getEventName() != null)
//                .collect(Collectors.groupingBy(Registration::getEventName, Collectors.counting()));
//
//        model.addAttribute("eventStats", eventStats);
//        model.addAttribute("eventStats", eventStats);
        // 1. Fetch the data from the database first!
        List<Registration> all = registrationRepository.findAll();

// 2. Now you can safely create the stream
        Map<String, Long> eventStats = all.stream()
                .filter(r -> r != null && r.getEventName() != null)
                .collect(Collectors.groupingBy(Registration::getEventName, Collectors.counting()));

        model.addAttribute("eventStats", eventStats);

        // 📊 Fetch all registrations
        all = registrationRepository.findAll();

        long total = all.size();

        long online = all.stream()
                .filter(r -> "ONLINE".equalsIgnoreCase(r.getPaymentMode()))
                .count();

        long cash = all.stream()
                .filter(r -> "CASH".equalsIgnoreCase(r.getPaymentMode()))
                .count();

        long uniqueEvents = all.stream()
                .map(Registration::getEventName)
                .distinct()
                .count();

        // 📦 Send to dashboard
        model.addAttribute("total", total);
        model.addAttribute("online", online);
        model.addAttribute("cash", cash);
        model.addAttribute("eventCount", uniqueEvents);

        return "admin-dashboard";
    }

    @GetMapping("/admin/export/excel")
    public void exportExcel(
            HttpSession session,
            HttpServletResponse response,
            @RequestParam(required = false) String event) throws Exception {

        Boolean loggedIn = (Boolean) session.getAttribute("ADMIN_LOGGED_IN");
        if (loggedIn == null || !loggedIn) {
            response.sendRedirect("/admin/login");
            return;
        }

        // 🔎 Filter by event if provided
        List<Registration> list;

        if (event != null && !event.isEmpty()) {
            list = registrationRepository.findByEventName(event);
        } else {
            list = registrationRepository.findAll();
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Registrations");

        // Header
        Row header = sheet.createRow(0);

        String[] cols = {
                "ID", "Name", "Roll", "Dept", "Year",
                "Phone", "Email", "Event", "Group Members", // 🚀 Added Group Members
                "Payment Mode", "Transaction ID", "Screenshot URL" // 🚀 Added Transaction ID
        };

        for (int i = 0; i < cols.length; i++) {
            header.createCell(i).setCellValue(cols[i]);
        }

        // Data
        // Data
        int rowNum = 1;
        for (Registration r : list) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(r.getId());
            row.createCell(1).setCellValue(r.getStudentName());
            row.createCell(2).setCellValue(r.getRollNo());
            row.createCell(3).setCellValue(r.getDepartment());
            row.createCell(4).setCellValue(r.getYear());
            row.createCell(5).setCellValue(r.getPhone());
            row.createCell(6).setCellValue(r.getEmail());
            row.createCell(7).setCellValue(r.getEventName());

            // 🚀 8. Group Members
            row.createCell(8).setCellValue(r.getGroupMembers() != null ? r.getGroupMembers() : "—");

            // 🚀 9. Payment Mode (Will now correctly show "FREE", "CASH", or "ONLINE")
            row.createCell(9).setCellValue(r.getPaymentMode());

            // 🚀 10. Transaction ID
            row.createCell(10).setCellValue(r.getTransactionId() != null ? r.getTransactionId() : "—");

            // 🚀 11. Screenshot URL
            row.createCell(11).setCellValue(r.getScreenshotUrl());
        }

//        for (int i=0;i<cols.length;i++) sheet.autoSizeColumn(i);
        for (int i = 0; i < cols.length; i++) sheet.autoSizeColumn(i);

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        String filename = (event == null || event.isEmpty())
                ? "all-registrations.xlsx"
                : event.replace(" ","_") + ".xlsx";

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=" + filename);

        workbook.write(response.getOutputStream());
        workbook.close();
    }


}
