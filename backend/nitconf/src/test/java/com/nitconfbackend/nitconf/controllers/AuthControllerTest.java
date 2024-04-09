package com.nitconfbackend.nitconf.controllers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nitconfbackend.nitconf.models.ConferencePaper;
import com.nitconfbackend.nitconf.models.User;
import com.nitconfbackend.nitconf.repositories.UserRepository;
import com.nitconfbackend.nitconf.service.AuthenticationService;
import com.nitconfbackend.nitconf.service.JwtService;
import com.nitconfbackend.nitconf.types.*;




public class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // userRepo = mock(UserRepository.class);
    }

    @Test
    void testLoginWithValidCredentials() {
        AuthenticationRequest user = new AuthenticationRequest("john@doe.com", "samplePassword");
        when(authenticationService.login(user)).thenReturn(new AuthenticationResponse("sample_token", "User logged in successfully"));

        ResponseEntity<AuthenticationResponse> response = authController.login(user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testLoginWithNullRequest() {
        ResponseEntity<AuthenticationResponse> responseEntity = authController.login(null);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testLoginWithNullEmail() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail(null);
        authenticationRequest.setPassword("pswd1");

        ResponseEntity<AuthenticationResponse> responseEntity = authController.login(authenticationRequest);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testLoginWithNullPassword() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("hello@gmail.com");
        authenticationRequest.setPassword(null);

        ResponseEntity<AuthenticationResponse> responseEntity = authController.login(authenticationRequest);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }


    @Test
    void testRegisterUserWithNullUser() {
        ResponseEntity<AuthenticationResponse> responseEntity = authController.registerUser(null);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testRegisterUserWithNullFields() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFirstName(null);
        registerRequest.setLastName(null);
        registerRequest.setEmail(null);
        registerRequest.setPassword(null);

        ResponseEntity<AuthenticationResponse> responseEntity = authController.registerUser(registerRequest);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testRegisterUserWithPartialNullFeilds() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFirstName("Arjun");
        registerRequest.setLastName("Das");
        registerRequest.setEmail(null);
        registerRequest.setPassword("das@123");

        ResponseEntity<AuthenticationResponse> responseEntity = authController.registerUser(registerRequest);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    // @Test
    // void testRegisterUserWithExistingEmail() {
    //     List<ConferencePaper> conferencePapers = new ArrayList<>();
    //     User sampleUser = new User("4324", "Arjun", "Das", "das@gmail.com", "das@123", true, conferencePapers);

    //     RegisterRequest registerRequest = new RegisterRequest("Arjun", "Das", "das@gmial.com", "das@123");
    //     when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(sampleUser));
    //     // when(authenticationService.register(sampleUser)).thenReturn(new AuthenticationResponse("sample_token", "User registered successfully"));
    //     ResponseEntity<AuthenticationResponse> responseEntity = authController.registerUser(registerRequest);
    //     assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    // }
    // @Test
    // void testRegisterUser() {
    //     User sampleUser = new User(
    //             "4324",
    //             "Arjun",
    //             "Das",
    //             "das@gmail.com",
    //             "das@123",
    //             true,
    //             new ArrayList<>());
    //     RegisterRequest registerRequest = new RegisterRequest(sampleUser.getFirstName(), sampleUser.getLastName(),
    //             sampleUser.getEmail(), sampleUser.getPassword());

    //     String jwtToken = "eyj";
    //     AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
    //             .msg("User registered successfully").token(jwtToken).build();
    //     // stubbing
    //     when(jwtService.generateToken(sampleUser)).thenReturn(jwtToken);
    //     // when(userRepo.save(sampleUser)).thenReturn(sampleUser);
    //     when(service.register(registerRequest)).thenReturn(authenticationResponse);
    //     when(userRepo.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());

    //     ResponseEntity<AuthenticationResponse> responseEntity = authController.registerUser(registerRequest);
    //     assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    // }


}