package com.nitconfbackend.nitconf.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nitconfbackend.nitconf.models.User;
import com.nitconfbackend.nitconf.repositories.UserRepository;
import com.nitconfbackend.nitconf.service.AuthenticationService;
import com.nitconfbackend.nitconf.types.AuthenticationRequest;
import com.nitconfbackend.nitconf.types.AuthenticationResponse;
import com.nitconfbackend.nitconf.types.RegisterRequest;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

// @CrossOrigin(origins = "http://localhost:3004")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationService service;

    @Autowired
    private UserRepository userRepo;

   
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody RegisterRequest user) {
        if(user == null){
            System.out.println("User is null");
            return ResponseEntity.badRequest().build();
        }
        else{
            // System.out.println("User is not null");
        }

        if (user.getFirstName() == null || user.getLastName() == null || user.getEmail() == null || user.getPassword() == null )
            return ResponseEntity.badRequest().build();
        Optional<User> userExists = userRepo.findByEmail(user.getEmail());
        if (userExists.isPresent()){
            String errorMessage = "User with email " + user.getEmail() + " already exists";
            System.out.println(errorMessage);
            AuthenticationResponse response = AuthenticationResponse.builder().msg(errorMessage).build();
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(service.register(user));
    }

   
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest user) {
        
        if (user.getEmail() == null || user.getPassword() == null){
            System.out.println("User is null");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.login(user));
    }

}

