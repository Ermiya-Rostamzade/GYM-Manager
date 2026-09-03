package com.gym.management.controller;

import com.gym.management.dto.request.LockerCreateRequest;
import com.gym.management.dto.request.PlanCreateRequest;
import com.gym.management.dto.request.UserRegisterRequest;
import com.gym.management.entity.enums.GenderSection;
import com.gym.management.entity.enums.Role;
import com.gym.management.service.LockerService;
import com.gym.management.service.PlanService;
import com.gym.management.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final PlanService planService;
    private final LockerService lockerService;

    // ========================
    // USER MANAGEMENT
    // ========================

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/users/new")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new UserRegisterRequest(null, null, null, null));
        model.addAttribute("roles", Role.values());
        return "user-form";
    }

    @PostMapping("/users")
    public String createUser(
            @Valid @ModelAttribute("user") UserRegisterRequest request,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", Role.values());
            return "user-form";
        }
        userService.registerUser(request);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    // ========================
    // PLAN MANAGEMENT
    // ========================

    @GetMapping("/plans")
    public String getAllPlans(Model model) {
        model.addAttribute("plans", planService.getAllPlans());
        return "plans";
    }

    @GetMapping("/plans/new")
    public String showCreatePlanForm(Model model) {
        model.addAttribute("plan", new PlanCreateRequest(null, null, null, null, null));
        return "plan-form";
    }

    @PostMapping("/plans")
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
    public String deletePlan(@PathVariable long id) {
        planService.deletePlan(id);
        return "redirect:/admin/plans";
    }

    // ========================
    // LOCKER MANAGEMENT
    // ========================

    @GetMapping("/lockers/new")
    public String showCreateLockerForm(Model model) {
        model.addAttribute("lockerRequest", new LockerCreateRequest("", GenderSection.MEN, ""));
        model.addAttribute("sections", GenderSection.values());
        return "admin/locker-create";
    }

    @PostMapping("/lockers")
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
            redirectAttributes.addFlashAttribute("successMessage", "کمد با موفقیت تعریف شد.");
            return "redirect:/lockers?section=" + request.genderSection();
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("sections", GenderSection.values());
            return "admin/locker-create";
        }
    }
}