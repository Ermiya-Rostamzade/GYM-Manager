package com.gym.management.controller;


import com.gym.management.dto.request.BuySubscriptionRequest;
import com.gym.management.dto.response.UserResponse;
import com.gym.management.service.PlanService;
import com.gym.management.service.SubscriptionService;
import com.gym.management.service.UserService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private  final SubscriptionService subscriptionService;
    private  final PlanService planService;
    private  final UserService userService;

    @GetMapping("/buy")
    public String showBuyForm(Model model){
        model.addAttribute("plans",planService.getAllPlans());
        return "buy-plan";
    }

    @PostMapping("/purchase")
    public String purchasePlan(
            @Valid @ModelAttribute BuySubscriptionRequest request,
            BindingResult bindingResult,
            Principal principal,
            Model model
    ){
        if (bindingResult.hasErrors()) {
            model.addAttribute("plans", planService.getAllPlans());
            return "buy-plan";
        }
        UserResponse user = userService.getUserByMobileNumber(principal.getName());
        try{
            subscriptionService.buyAndActivateDirectly(user.id(),  request);
            return "redirect:/subscriptions/my-subscriptions?success";
        }catch(IllegalArgumentException | IllegalStateException e){
            model.addAttribute("plans", planService.getAllPlans());
            model.addAttribute("errorMessage", e.getMessage());
            return "buy-plan";
        }
    }

    @GetMapping("/my-subscriptions")
    public String showMySubscriptions(Model model, Principal principal){
        UserResponse user = userService.getUserByMobileNumber(principal.getName());
        model.addAttribute("user",user);
        model.addAttribute("subscriptions", subscriptionService.getUserSubscriptions(user.id()));
        return "my-subscriptions";
    }


}
