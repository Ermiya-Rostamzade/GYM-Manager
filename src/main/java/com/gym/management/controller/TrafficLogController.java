package com.gym.management.controller;

import com.gym.management.dto.request.TrafficLogUserRequest;
import com.gym.management.dto.response.TrafficLogResponse;
import com.gym.management.dto.response.UserResponse;
import com.gym.management.service.TrafficLogService;
import com.gym.management.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/traffic-log")
@RequiredArgsConstructor
public class TrafficLogController {

    private final TrafficLogService trafficLogService;
    private  final UserService userService;

    @GetMapping
    public String trafficLogs(Model model) {
        model.addAttribute(
                "trafficLogs", trafficLogService.getAllTrafficLogs()
        );
        return "traffic-logs";
    }

    @GetMapping("/check-in")
    public String showCheckInForm(Model model) {
        model.addAttribute(
                "trafficLogCheckIn",
                new TrafficLogUserRequest(null)
        );
        return "traffic-log-check-in";
    }

    @PostMapping("/check-in")
    public String checkIn(
            @Valid @ModelAttribute("trafficLogCheckIn") TrafficLogUserRequest request,
            BindingResult bindingResult,
            Principal principal,
            Model model
    ) {
        UserResponse user = userService.getUserByMobileNumber(principal.getName());
        TrafficLogUserRequest userRequest = new TrafficLogUserRequest(request.method());

        if (bindingResult.hasErrors()) {
            return "traffic-log-check-in";
        }
        try{

            trafficLogService.createTrafficLog(user.id(),request);
            return "redirect:/traffic-log";
        }catch (IllegalArgumentException | IllegalStateException ex){
            model.addAttribute("error", ex.getMessage());
            return "traffic-log-check-in";
        }


    }

    @GetMapping("/check-out/{id}")
    public String showCheckOutForm(@PathVariable Long id, Model model) {
        model.addAttribute("trafficLogId", id);
        return "traffic-log-check-out";
    }

    @PostMapping("/check-out/{id}")
    public String checkOut(@PathVariable Long id,Model model) {
        try {
            trafficLogService.setTrafficLogCheckOutTime(id);
            return "redirect:/traffic-log";
        } catch (IllegalStateException | IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("trafficLogId", id);
            return "traffic-log-check-out";
        }
    }

}
