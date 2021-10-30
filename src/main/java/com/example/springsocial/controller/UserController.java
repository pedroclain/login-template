package com.example.springsocial.controller;

import com.example.springsocial.model.ApplicationUser;
import com.example.springsocial.repository.ApplicationUserRepository;
import com.example.springsocial.security.TokenProvider;
import com.example.springsocial.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;
    @Autowired
    private TokenProvider tokenProvider;

    @GetMapping
    public String getIndexPage(Model model, HttpServletRequest request) {
        Optional<ApplicationUser> userOptional = getApplicationUser(request);
        userOptional.ifPresent(applicationUser -> model.addAttribute("user", applicationUser));
        return "index";
    }

    @GetMapping("/profile")
    public String getProfilePage(Model model, HttpServletRequest request) {
        ApplicationUser applicationUser = getApplicationUser(request).get();
        model.addAttribute("user", applicationUser);
        return "profile";
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, "Authorization");
        return "redirect:/login?logout";
    }

    private Optional<ApplicationUser> getApplicationUser(HttpServletRequest request) {
        Optional<Cookie> cookieOptional = CookieUtils.getCookie(request, "Authorization");
        if (cookieOptional.isPresent()) {
            String token = cookieOptional.get().getValue();
            if (tokenProvider.validateToken(token)) {
                return applicationUserRepository.findByEmail(tokenProvider.getUsername(token));
            }
        }
        return Optional.empty();
    }
}
