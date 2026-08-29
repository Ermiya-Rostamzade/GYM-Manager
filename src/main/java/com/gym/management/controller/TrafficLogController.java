package com.gym.management.controller;

import com.gym.management.dto.request.TrafficLogUserRequest;
import com.gym.management.service.TrafficLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/traffic-log")
@RequiredArgsConstructor
public class TrafficLogController {

    private final TrafficLogService trafficLogService;

    @GetMapping
    public String trafficLogs(Model model) {
        model.addAttribute(
                "trafficLogs", trafficLogService.getAllTrafficLogs(10)
        );
        return "traffic-logs";
    }

    @GetMapping("/check-in")
    public String showCheckInForm(Model model) {
        model.addAttribute(
                "trafficLogCheckIn",
                new TrafficLogUserRequest(null, null)
        );
        return "traffic-log-check-in";
    }

    @PostMapping("/check-in")
    public String checkIn(
            @Valid @ModelAttribute("trafficLogCheckIn") TrafficLogUserRequest request
    ) {
        trafficLogService.createTrafficLog(request);
        return "redirect:/traffic-log";
    }

    @GetMapping("/check-out/{id}")
    public String showCheckOutForm(@PathVariable Long id, Model model) {
        model.addAttribute("trafficLogId", id);
        return "traffic-log-check-out";
    }

    @PostMapping("/check-out/{id}")
    public String checkOut(@PathVariable Long id) {
        trafficLogService.setTrafficLogCheckOutTime(id);
        return "redirect:/traffic-log";
    }

}
