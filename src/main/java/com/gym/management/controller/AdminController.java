package com.gym.management.controller;

import com.gym.management.dto.request.LockerCreateRequest;
import com.gym.management.dto.request.PlanCreateRequest;
import com.gym.management.dto.request.UserRegisterRequest;
import com.gym.management.entity.enums.GenderSection;
import com.gym.management.entity.enums.Role;
import com.gym.management.service.LockerService;
import com.gym.management.service.PlanService;
import com.gym.management.service.TrafficLogService;
import com.gym.management.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Administration and reception operations.
 */
@Controller
@RequestMapping({"/admin", "/reception", "/receptionist"})
@RequiredArgsConstructor
public class AdminController {

    private final LockerService lockerService;
    private final PlanService planService;
    private final TrafficLogService trafficLogService;
    private final UserService userService;

    @GetMapping({"", "/"})
    public String adminHome() {
        return "admin/dashboard";
    }

    @GetMapping({"/users"})
    @PreAuthorize("hasRole('ADMIN')")
    public String users(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/users/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String userForm(Model model) {
        model.addAttribute("user", new UserRegisterRequest(null, null, null, null));
        model.addAttribute("roles", Role.values());
        return "user-form";
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public String createUser(
            @Valid @ModelAttribute("user") UserRegisterRequest request,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", Role.values());
            return "user-form";
        }
        try {
            userService.registerUser(request);
            return "redirect:/admin/users";
        } catch (IllegalArgumentException ex) {
            result.rejectValue("mobileNumber", "duplicate", ex.getMessage());
            model.addAttribute("roles", Role.values());
            return "user-form";
        }
    }

    @PostMapping("/users/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/plans")
    @PreAuthorize("hasRole('ADMIN')")
    public String plans(Model model) {
        model.addAttribute("plans", planService.getAllPlans());
        return "plans";
    }

    @GetMapping("/plans/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String planForm(Model model) {
        model.addAttribute("plan", new PlanCreateRequest(null, null, null, null, null));
        return "plan-form";
    }

    @PostMapping("/plans")
    @PreAuthorize("hasRole('ADMIN')")
    public String createPlan(
            @Valid @ModelAttribute("plan") PlanCreateRequest request,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return "plan-form";
        }
        try {
            planService.createPlan(request);
            return "redirect:/admin/plans";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "plan-form";
        }
    }

    @PostMapping("/plans/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deletePlan(@PathVariable long id) {
        planService.deletePlan(id);
        return "redirect:/admin/plans";
    }

    @GetMapping("/lockers/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String lockerForm(Model model) {
        model.addAttribute("lockerRequest", new LockerCreateRequest("", GenderSection.MEN, ""));
        model.addAttribute("sections", GenderSection.values());
        return "admin/locker-create";
    }

    @PostMapping("/lockers")
    @PreAuthorize("hasRole('ADMIN')")
    public String createLocker(
            @Valid @ModelAttribute("lockerRequest") LockerCreateRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("sections", GenderSection.values());
            return "admin/locker-create";
        }
        try {
            lockerService.createLocker(request);
            redirectAttributes.addFlashAttribute("successMessage", "Locker created successfully.");
            return "redirect:/dashboard/lockers?section=" + request.genderSection();
        } catch (RuntimeException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("sections", GenderSection.values());
            return "admin/locker-create";
        }
    }

    @GetMapping("/traffic")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public String trafficLogs(Model model) {
        model.addAttribute("trafficLogs", trafficLogService.getAllTrafficLogs());
        model.addAttribute("trafficBasePath", "/reception");
        return "traffic-logs";
    }

    @PostMapping("/traffic/check-out/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public String staffCheckOut(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            trafficLogService.setTrafficLogCheckOutTimeByStaff(id);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/reception/traffic";
    }
}
