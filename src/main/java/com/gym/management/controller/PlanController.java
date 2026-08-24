package com.gym.management.controller;

import com.gym.management.dto.request.PlanCreateRequest;
import com.gym.management.service.PlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @GetMapping
    public String getAllPlans(Model model) {
        model.addAttribute("plans", planService.getAllPlans());
        return "plans";
    }

    @GetMapping("/new")
    public String showCreatePlanForm(Model model) {
        model.addAttribute("plan", new PlanCreateRequest(
                null,
                null,
                null,
                null
        ));
        return "plan-form";
    }

    @PostMapping
    public String createPlan(
            @Valid @ModelAttribute("plan") PlanCreateRequest request
    ) {
        planService.createPlan(request);
        return "redirect:/admin/plans";
    }

    @PostMapping("/delete/{id}")
    public String deletePlan(@PathVariable long id) {
    planService.deletePlan(id);
    return "redirect:/admin/plans";
    }


}
