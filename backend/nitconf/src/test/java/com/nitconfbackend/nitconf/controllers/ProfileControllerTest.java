package com.nitconfbackend.nitconf.controllers;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

 
import com.nitconfbackend.nitconf.models.User;
import com.nitconfbackend.nitconf.repositories.UserRepository;
 

public class ProfileControllerTest {

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private ProfileController profileController;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }
 

    @Test
    void testProfileDetails() {
        User sampleUser = new User(
                "49832",
                "Arun",
                "Kumar",
                "arun@gmail.com",
                "arun@123",
                true,
                new ArrayList<>());
        when(authentication.getPrincipal()).thenReturn(sampleUser);
        ResponseEntity<User> responseEntity = profileController.profileDetails();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    
    @Test
    void testProfileDetailsThrowsException() {
        when(authentication.getPrincipal()).thenThrow(new NoSuchElementException());
        assertThrows(NoSuchElementException.class, () -> {
            profileController.profileDetails();
        });
    }
    
    @Test
    void testGetUser(){
        User sampleUser = new User(
            "49832",
            "Arun",
            "Kumar",
            "arun@gmail.com",
            "arun@123",
            true,
            new ArrayList<>());
        when(authentication.getName()).thenReturn(sampleUser.getEmail());
        when(userRepo.findById("49832")).thenReturn(Optional.of(sampleUser));
        ResponseEntity<User> responseEntity = profileController.getUser("49832");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }


    @Test
    void testGetUserInvalidId(){
        String invalidId = "12345";
        User sampleUser = new User(
            "3822",
            "Hemanth",
            "Sai",
            "sai@gmail.com",
            "sai@123",
            true,
            new ArrayList<>());
        when(authentication.getName()).thenReturn(sampleUser.getEmail());
        when(userRepo.findByEmail(sampleUser.getEmail())).thenReturn(Optional.of(sampleUser));
        when(userRepo.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            profileController.getUser(invalidId);
        });
    }
}
