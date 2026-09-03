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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

/**
 * Authenticated member dashboard. The legacy routes remain as aliases so existing
 * bookmarks and links continue to work while dashboard URLs are canonical.
 */
@Controller
@RequestMapping
@RequiredArgsConstructor
public class DashboardController {

    private final LockerService lockerService;
    private final PlanService planService;
    private final SubscriptionService subscriptionService;
    private final TrafficLogService trafficLogService;
    private final UserService userService;

    @GetMapping({"/dashboard", "/subscriptions/my-subscriptions"})
    public String dashboard(Model model, Principal principal) {
        UserResponse user = currentUser(principal);
        model.addAttribute("user", user);
        model.addAttribute("subscriptions", subscriptionService.getUserSubscriptions(user.id()));
        return "my-subscriptions";
    }

    @GetMapping({"/dashboard/subscriptions/new", "/subscriptions/buy"})
    public String subscriptionForm(Model model) {
        model.addAttribute("plans", planService.getAllPlans());
        return "buy-plan";
    }

    @PostMapping({"/dashboard/subscriptions", "/subscriptions/purchase"})
    public String purchase(
            @Valid @ModelAttribute BuySubscriptionRequest request,
            BindingResult bindingResult,
            Principal principal,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("plans", planService.getAllPlans());
            return "buy-plan";
        }
        UserResponse user = currentUser(principal);
        try {
            subscriptionService.buyAndActivateDirectly(user.id(), request);
            return "redirect:/dashboard?success";
        } catch (IllegalArgumentException | IllegalStateException ex) {
            model.addAttribute("plans", planService.getAllPlans());
            model.addAttribute("errorMessage", ex.getMessage());
            return "buy-plan";
        }
    }

    @GetMapping({"/dashboard/lockers", "/lockers"})
    public String lockers(
            @RequestParam(name = "section", defaultValue = "MEN") GenderSection section,
            Principal principal,
            Model model) {
        UserResponse user = currentUser(principal);
        LockerReservation activeReservation = lockerService.getActiveReservationForUser(user.id());
        model.addAttribute("currentSection", section);
        model.addAttribute("lockers", lockerService.getAllLockerBySection(section));
        model.addAttribute("activeReservation", activeReservation);
        return "lockers";
    }

    @PostMapping({"/dashboard/lockers/reservations", "/lockers/reserve"})
    public String reserveLocker(
            @RequestParam("lockerId") Long lockerId,
            @RequestParam("section") GenderSection section,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        try {
            lockerService.reserveLocker(currentUser(principal).id(), lockerId);
            redirectAttributes.addFlashAttribute("successMessage", "Locker reserved successfully.");
        } catch (IllegalStateException | IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/dashboard/lockers?section=" + section;
    }

    @PostMapping({"/dashboard/lockers/reservations/release", "/lockers/release"})
    public String releaseLocker(
            @RequestParam("section") GenderSection section,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        try {
            lockerService.releaseLocker(currentUser(principal).id());
            redirectAttributes.addFlashAttribute("successMessage", "Locker released successfully.");
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/dashboard/lockers?section=" + section;
    }

    @GetMapping({"/dashboard/traffic", "/traffic-log"})
    public String traffic(Model model, Principal principal) {
        UserResponse user = currentUser(principal);
        model.addAttribute("trafficLogs", trafficLogService.getTrafficLogsForUser(user.id()));
        return "traffic-logs";
    }

    @GetMapping({"/dashboard/traffic/check-in", "/traffic-log/check-in", "/reception/traffic/check-in"})
    public String checkInForm(Model model) {
        model.addAttribute("trafficLogCheckIn", new TrafficLogUserRequest(null));
        return "traffic-log-check-in";
    }

    @PostMapping({"/dashboard/traffic/check-in", "/traffic-log/check-in", "/reception/traffic/check-in"})
    public String checkIn(
            @Valid @ModelAttribute("trafficLogCheckIn") TrafficLogUserRequest request,
            BindingResult bindingResult,
            Principal principal,
            Model model) {
        if (bindingResult.hasErrors()) {
            return "traffic-log-check-in";
        }
        try {
            trafficLogService.createTrafficLog(currentUser(principal).id(), request);
            return "redirect:/dashboard/traffic";
        } catch (IllegalArgumentException | IllegalStateException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "traffic-log-check-in";
        }
    }

    @GetMapping({"/dashboard/traffic/check-out/{id}", "/traffic-log/check-out/{id}"})
    public String checkOutForm(@PathVariable Long id, Model model) {
        model.addAttribute("trafficLogId", id);
        return "traffic-log-check-out";
    }

    @PostMapping({"/dashboard/traffic/check-out/{id}", "/traffic-log/check-out/{id}"})
    public String checkOut(@PathVariable Long id, Principal principal, Model model) {
        try {
            trafficLogService.setTrafficLogCheckOutTime(id, currentUser(principal).id());
            return "redirect:/dashboard/traffic";
        } catch (IllegalStateException | IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("trafficLogId", id);
            return "traffic-log-check-out";
        }
    }

    private UserResponse currentUser(Principal principal) {
        return userService.getUserByMobileNumber(principal.getName());
    }
}
