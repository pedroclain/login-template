package com.example.springsocial.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorControllerImp implements ErrorController {

    private final String ERROR_404_MESSAGE = "Page not found!";
    private final String ERROR_500_MESSAGE = "Internal error!";

    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object code = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (code != null) {
            model.addAttribute("code", String.valueOf(code));
            switch (String.valueOf(code)) {
                case "404":
                    model.addAttribute("message", ERROR_404_MESSAGE);
                    break;
                case "500":
                    model.addAttribute("message", ERROR_500_MESSAGE);
                    break;
            }
        }
        return "error";
    }
}
