package com.example.springsocial.repository;

import com.example.springsocial.model.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {

    Optional<ApplicationUser> findByEmail(String email);
    @Query("update ApplicationUser u set u.name = ?2 where u.id = ?1")
    @Transactional
    @Modifying
    void updateName(long id, String name);
    @Query("update ApplicationUser u set u.email = ?2 where u.id = ?1")
    @Transactional
    @Modifying
    void updateEmail(long id, String email);
    @Query("update ApplicationUser u set u.password = ?2 where u.id = ?1")
    @Transactional
    @Modifying
    void updatePassword(long id, String password);
}
