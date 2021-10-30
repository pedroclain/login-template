package com.example.springsocial.controller;

import com.example.springsocial.exception.BadRequestException;
import com.example.springsocial.model.ApplicationUser;
import com.example.springsocial.model.Provider;
import com.example.springsocial.payload.LoginRequest;
import com.example.springsocial.payload.SignUpRequest;
import com.example.springsocial.repository.ApplicationUserRepository;
import com.example.springsocial.security.TokenProvider;
import com.example.springsocial.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @PostMapping("/authenticate")
    public String authenticateUser(@Valid @ModelAttribute LoginRequest loginRequest,
                                              HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);
        CookieUtils.addCookie(response, "Authorization", token, 3600);
        return "redirect:/profile";
    }

    @GetMapping("/signup")
    public String getSignUpPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@Valid @ModelAttribute SignUpRequest signUpRequest) {
        if(applicationUserRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new BadRequestException("Email address already in use.");
        }

        // Creating applicationUser's account
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setName(signUpRequest.getName());
        applicationUser.setEmail(signUpRequest.getEmail());
        applicationUser.setPassword(signUpRequest.getPassword());
        applicationUser.setProvider(Provider.local);
        applicationUser.setPassword(passwordEncoder.encode(applicationUser.getPassword()));
        applicationUser.setRoles("USER");
        applicationUserRepository.save(applicationUser);

        return "redirect:/login";
    }

}
