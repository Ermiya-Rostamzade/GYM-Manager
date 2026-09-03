package com.gym.management.controller;

import com.gym.management.dto.response.UserResponse;
import com.gym.management.entity.LockerReservation;
import com.gym.management.entity.enums.GenderSection;
import com.gym.management.service.LockerService;
import com.gym.management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/lockers")
@RequiredArgsConstructor
public class LockerController {

    private final LockerService lockerService;
    private final UserService userService;


    @GetMapping
    public String showLockerGrid(
            @RequestParam(name = "section", defaultValue = "MEN") GenderSection section,
            Principal principal,
            Model model
    ) {
        UserResponse user = userService.getUserByMobileNumber(principal.getName());
        LockerReservation activeReservation = lockerService.getActiveReservationForUser(user.id());

        model.addAttribute("currentSection", section);
        model.addAttribute("lockers", lockerService.getAllLockerBySection(section));
        model.addAttribute("activeReservation", activeReservation);
        return "lockers";
    }


    @PostMapping("/reserve")
    public String reserveLocker(
            @RequestParam("lockerId") Long lockerId,
            @RequestParam("section") GenderSection section,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        UserResponse user = userService.getUserByMobileNumber(principal.getName());
        try {
            lockerService.reserveLocker(user.id(), lockerId);
            redirectAttributes.addFlashAttribute("successMessage", "Locker reserved successfully.");
        } catch (IllegalStateException | IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/lockers?section=" + section;
    }


    @PostMapping("/release")
    public String releaseLocker(
            @RequestParam("section") GenderSection section,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        UserResponse user = userService.getUserByMobileNumber(principal.getName());
        try {
            lockerService.releaseLocker(user.id());
            redirectAttributes.addFlashAttribute("successMessage", "Locker released successfully.");
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/lockers?section=" + section;
    }
}