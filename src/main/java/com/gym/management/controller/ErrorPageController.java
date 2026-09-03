package com.gym.management.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Consistent, user-friendly pages for authorization and application errors.
 */
@Controller
public class ErrorPageController implements ErrorController {

    @RequestMapping("/error/403")
    public String forbidden(Model model) {
        model.addAttribute("status", 403);
        model.addAttribute("message", "You do not have permission to access this page.");
        return "error/403";
    }

    @RequestMapping("/error/404")
    public String notFound(Model model) {
        model.addAttribute("status", 404);
        model.addAttribute("message", "The page you requested could not be found.");
        return "error/404";
    }

    @RequestMapping("/error")
    public String error(HttpServletRequest request, Model model) {
        int status = 500;
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode != null) {
            try {
                status = Integer.parseInt(statusCode.toString());
            } catch (NumberFormatException ignored) {
                // Keep the safe generic status.
            }
        }
        model.addAttribute("status", status);
        model.addAttribute("message", status == 404
                ? "The page you requested could not be found."
                : "Something went wrong while processing your request.");
        return status == 404 ? "error/404" : "error/500";
    }
}
