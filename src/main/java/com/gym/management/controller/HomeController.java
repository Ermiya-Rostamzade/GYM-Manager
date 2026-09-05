package com.gym.management.controller;

import com.gym.management.dto.request.UserRegisterRequest;
import com.gym.management.entity.enums.Role;
import com.gym.management.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;

    @GetMapping("/")
    public String root() {
        return "home";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserRegisterRequest(null, null, null, Role.ATHLETE));
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserRegisterRequest userRegisterRequest,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            userService.registerSelf(userRegisterRequest);
            return "redirect:/login?registered";
        } catch (IllegalArgumentException ex) {
            bindingResult.rejectValue("mobileNumber", null, ex.getMessage());
            return "register";
        }
    }

    @GetMapping("/403")
    public String accessDenied() {
        return "403";
    }
}