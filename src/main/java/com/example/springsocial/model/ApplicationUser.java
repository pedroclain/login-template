package com.example.springsocial.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationUser extends AbstractEntity {
    
    protected String name;
    @Column(unique = true)
    protected String email;
    @ToString.Exclude
    protected String password;
    @Enumerated(EnumType.STRING)
    protected Provider provider;
    protected String roles;

    public ApplicationUser(ApplicationUser applicationUser) {
        this.name = applicationUser.getName();
        this.email = applicationUser.getEmail();
        this.password = applicationUser.getPassword();
        this.provider = applicationUser.getProvider();
        this.roles = applicationUser.getRoles();
    }
}
