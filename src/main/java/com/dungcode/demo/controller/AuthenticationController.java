package com.example.api.demo.spring.boot.controller;

import com.example.api.demo.spring.boot.dto.request.AuthenticationRequest;
import com.example.api.demo.spring.boot.dto.request.IntrospectRequest;
import com.example.api.demo.spring.boot.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    ResponseEntity<?> authenticate(@RequestBody @Valid AuthenticationRequest request) {
        return (authenticationService.authenticate(request)).responseEntity();
    }

    @PostMapping("/introspect")
    ResponseEntity<?> introspectToken(@RequestBody @Valid IntrospectRequest request) throws ParseException, JOSEException {
        return (authenticationService.introspectToken(request)).responseEntity();
    }

}
