package com.example.springsocial.controller;

import com.example.springsocial.config.AuthProperties;
import com.example.springsocial.exception.BadRequestException;
import com.example.springsocial.model.ApplicationUser;
import com.example.springsocial.model.Provider;
import com.example.springsocial.payload.LoginRequest;
import com.example.springsocial.payload.SignUpRequest;
import com.example.springsocial.repository.ApplicationUserRepository;
import com.example.springsocial.security.TokenProvider;
import com.example.springsocial.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Controller
@RequestMapping
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthProperties authProperties;

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @PostMapping("/authenticate")
    public String authenticateUser(@ModelAttribute LoginRequest loginRequest,
                                              HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);
        CookieUtils.addCookie(response, authProperties.getCookie().getName(), token, authProperties.getCookie().getExpirationSec());
        return "redirect:/profile";
    }

    @GetMapping("/signup")
    public String getSignUpPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute SignUpRequest signUpRequest) {
        if(applicationUserRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new BadRequestException("Email address already in use.");
        }

        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setName(signUpRequest.getName());
        applicationUser.setEmail(signUpRequest.getEmail());
        applicationUser.setPassword(signUpRequest.getPassword());
        applicationUser.setProvider(Provider.local);
        applicationUser.setPassword(passwordEncoder.encode(applicationUser.getPassword()));
        applicationUser.setRoles("USER");
	    applicationUser.setImageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSisT6Nb6XIXyX7kQ9XmEMID6eSxl4mQ8E0vXbwc77pJqhZYUUdU13h7VRlt4rZqOgg5Yc&usqp=CAU");
        applicationUserRepository.save(applicationUser);

        return "redirect:/login";
    }

}
