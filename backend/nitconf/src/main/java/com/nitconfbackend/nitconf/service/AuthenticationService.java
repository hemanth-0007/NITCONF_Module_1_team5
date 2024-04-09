package com.nitconfbackend.nitconf.service;

import java.util.ArrayList;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// import com.nitconfbackend.nitconf.models.Role;
import com.nitconfbackend.nitconf.models.ConferencePaper;
import com.nitconfbackend.nitconf.models.User;
import com.nitconfbackend.nitconf.repositories.UserRepository;
import com.nitconfbackend.nitconf.types.AuthenticationRequest;
import com.nitconfbackend.nitconf.types.AuthenticationResponse;
import com.nitconfbackend.nitconf.types.RegisterRequest;

// import com.example.demo.config.JwtService;
// import com.example.demo.models.Role;
// import com.example.demo.models.User;
// import com.example.demo.respository.UserRepository;

import lombok.RequiredArgsConstructor;



/**
 * AuthenticationService
 * Service class for managing user authentication.
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new user.
     *
     * @param request The registration request containing user details.
     * @return AuthenticationResponse containing a JWT token and a message.
     */
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .conferencePapers(new ArrayList<ConferencePaper>())
                .isVerified(true)
                .build();
        if (user != null) {
            User savedUser = repository.save(user);
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .msg("User registered successfully")
                    .build();
        }
        return null;
    }
     /**
     * Performs user login.
     *
     * @param request The login request containing user credentials.
     * @return AuthenticationResponse containing a JWT token and a message.
     */
    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));
        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .msg("User logged in successfully")
                .build();
    }
}