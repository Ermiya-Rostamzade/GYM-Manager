package com.gym.management.controller;

import com.gym.management.dto.request.LockerCreateRequest;
import com.gym.management.entity.enums.GenderSection;
import com.gym.management.service.LockerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/lockers")
@RequiredArgsConstructor
public class AdminLockerController {

    private final LockerService lockerService;

    @GetMapping("/new")
    public String showCreateLockerForm(Model model) {
        model.addAttribute("lockerRequest", new LockerCreateRequest("", GenderSection.MEN, ""));
        model.addAttribute("sections", GenderSection.values());
        return "admin/locker-create";
    }

    @PostMapping
    public String createLocker(
            @Valid @ModelAttribute("lockerRequest") LockerCreateRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
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