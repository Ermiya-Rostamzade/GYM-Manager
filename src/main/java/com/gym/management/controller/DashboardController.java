package com.gym.management.controller;

import com.gym.management.dto.request.BuySubscriptionRequest;
import com.gym.management.dto.request.TrafficLogUserRequest;
import com.gym.management.dto.response.UserResponse;
import com.gym.management.entity.LockerReservation;
import com.gym.management.entity.enums.GenderSection;
import com.gym.management.service.LockerService;
import com.gym.management.service.PlanService;
import com.gym.management.service.SubscriptionService;
import com.gym.management.service.TrafficLogService;
import com.gym.management.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final SubscriptionService subscriptionService;
    private final PlanService planService;
    private final UserService userService;
    private final LockerService lockerService;
    private final TrafficLogService trafficLogService;

    @GetMapping
    public String showDashboard(Model model, Principal principal) {
        UserResponse user = userService.getUserByMobileNumber(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("subscriptions", subscriptionService.getUserSubscriptions(user.id()));
        return "my-subscriptions";
    }

    @GetMapping("/buy")
    public String showBuyForm(Model model) {
        model.addAttribute("plans", planService.getAllPlans());
        return "buy-plan";
    }

    @PostMapping("/purchase")
    public String purchasePlan(
            @Valid @ModelAttribute BuySubscriptionRequest request,
            BindingResult bindingResult,
            Principal principal,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("plans", planService.getAllPlans());
            return "buy-plan";
        }
        UserResponse user = userService.getUserByMobileNumber(principal.getName());
        try {
            subscriptionService.buyAndActivateDirectly(user.id(), request);
            return "redirect:/dashboard?success";
        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("plans", planService.getAllPlans());
            model.addAttribute("errorMessage", e.getMessage());
            return "buy-plan";
        }
    }

    @GetMapping("/lockers")
    public String showLockerGrid(
            @RequestParam(name = "section", defaultValue = "MEN") GenderSection section,
            Principal principal,
            Model model) {
        UserResponse user = userService.getUserByMobileNumber(principal.getName());
        LockerReservation activeReservation = lockerService.getActiveReservationForUser(user.id());
        model.addAttribute("currentSection", section);
        model.addAttribute("lockers", lockerService.getAllLockerBySection(section));
        model.addAttribute("activeReservation", activeReservation);
        return "lockers";
    }

    @PostMapping("/lockers/reserve")
    public String reserveLocker(
            @RequestParam("lockerId") Long lockerId,
            @RequestParam("section") GenderSection section,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        UserResponse user = userService.getUserByMobileNumber(principal.getName());
        try {
            lockerService.reserveLocker(user.id(), lockerId);
            redirectAttributes.addFlashAttribute("successMessage", "Locker reserved successfully.");
        } catch (IllegalStateException | IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/dashboard/lockers?section=" + section;
    }

    @PostMapping("/lockers/release")
    public String releaseLocker(
            @RequestParam("section") GenderSection section,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        UserResponse user = userService.getUserByMobileNumber(principal.getName());
        try {
            lockerService.releaseLocker(user.id());
            redirectAttributes.addFlashAttribute("successMessage", "Locker released successfully.");
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/dashboard/lockers?section=" + section;
    }

    @GetMapping("/traffic-log")
    public String trafficLogs(Principal principal, Model model) {
        UserResponse user = userService.getUserByMobileNumber(principal.getName());
        model.addAttribute("trafficLogs", trafficLogService.getTrafficLogsForUser(user.id()));
        return "traffic-logs";
    }

    @GetMapping("/traffic-log/check-in")
    public String showCheckInForm(Model model) {
        model.addAttribute("trafficLogCheckIn", new TrafficLogUserRequest(null));
        return "traffic-log-check-in";
    }

    @PostMapping("/traffic-log/check-in")
    public String checkIn(
            @Valid @ModelAttribute("trafficLogCheckIn") TrafficLogUserRequest request,
            BindingResult bindingResult,
            Principal principal,
            Model model) {
        UserResponse user = userService.getUserByMobileNumber(principal.getName());
        if (bindingResult.hasErrors()) {
            return "traffic-log-check-in";
        }
        try {
            trafficLogService.createTrafficLog(user.id(), request);
            return "redirect:/dashboard/traffic-log";
        } catch (IllegalArgumentException | IllegalStateException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "traffic-log-check-in";
        }
    }

    @PostMapping("/traffic-log/check-out/{id}")
    public String checkOut(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            UserResponse user = userService.getUserByMobileNumber(principal.getName());
            trafficLogService.setTrafficLogCheckOutTime(id, user.id());
        } catch (IllegalStateException | IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/dashboard/traffic-log";
    }
}