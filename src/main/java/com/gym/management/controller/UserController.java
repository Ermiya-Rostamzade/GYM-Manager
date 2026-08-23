package com.gym.management.controller;


import com.gym.management.dto.request.UserRegisterRequest;
import com.gym.management.dto.response.UserResponse;
import com.gym.management.entity.enums.Role;
import com.gym.management.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users",userService.getAllUsers());
        return "users";
    }

    @GetMapping("/new")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user",new UserRegisterRequest(null,null,null,null));
        model.addAttribute("roles", Role.values());
        return "user-form";
    }

    @PostMapping
    public String createUser(
            @Valid @ModelAttribute("user") UserRegisterRequest request,
            BindingResult result,
            Model model
    ){
        if(result.hasErrors()){
            model.addAttribute("roles",Role.values());
            return "user-form";
        }

        userService.registerUser(request);
        return "redirect:/admin/users";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }
}
