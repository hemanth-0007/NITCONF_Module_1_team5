package com.nitconfbackend.nitconf.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// import com.nitconfbackend.nitconf.models.Role;
import com.nitconfbackend.nitconf.models.User;
import com.nitconfbackend.nitconf.repositories.UserRepository;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * Controller class for managing user profile.
*/
@RestController
@RequestMapping("/api/profile")
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {

    @Autowired
    public UserRepository userRepo;
    /**
     * Retrieves details of the authenticated user's profile.
     *
     * @return ResponseEntity containing the user details.
     */
    @GetMapping("")
    public ResponseEntity<User> profileDetails() {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(user);   
    }


    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable @NonNull String id) {
        User user = userRepo.findById(id).orElseThrow();        
        if(user == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }
    
    
}