package com.example.springsocial.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "app.auth")
@Getter
@Setter
public class AuthProperties {

    private Token token;
    private Cookie cookie;

    @Getter
    @Setter
    public static class Token {
        private String secret = "04ca023b39512e46d0c2cf4b48d5aac61d34302994c87ed4eff225dcf3b0a218739f3897051a057f9b846a69ea2927a587044164b7bae5e1306219d50b588cb1";
        private long expirationMsc = 864000000;
    }

    @Getter
    @Setter
    public static class Cookie {
        private String name = "token";
        private int expirationSec = 36000;
    }

}
