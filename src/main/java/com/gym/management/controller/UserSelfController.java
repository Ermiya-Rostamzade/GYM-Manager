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
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class UserSelfController {

    private final UserService userService;

    @GetMapping("/login")
    public String showLoginForm(Model model){
        return "login";
    }

    @GetMapping("/register")
    public String ShowRegisterForm(Model model) {
        model.addAttribute("user", new UserRegisterRequest(null,null,null, Role.ATHLETE));
        return "register";
    }
    @PostMapping("/register")
    public String RegisterUser(@Valid @ModelAttribute("user") UserRegisterRequest userRegisterRequest , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            userService.registerSelf(userRegisterRequest);
            return "redirect:/login?registered";
        }catch (IllegalArgumentException ex){
            bindingResult.rejectValue("mobileNumber", null, ex.getMessage());
            return "register";
        }
    }
}
