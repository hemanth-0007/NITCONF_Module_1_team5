package com.nitconfbackend.nitconf.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.nitconfbackend.nitconf.repositories.UserRepository;
import com.nitconfbackend.nitconf.models.User; 

public class ProfileControllerTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProfileController profileController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // @Test
    // public void testGetProfile() {
    //     String id = "1234";
    //     User user = new User();
    //     user.setId(id);
    //     when(profileController.profileDetails()).thenReturn(ResponseEntity.ok(user));

    //     assertEquals(user, profileController.profileDetails().getBody());
    // }
}
