package com.dungcode.demo.controller;

import com.dungcode.demo.dto.request.AuthenticationRequest;
import com.dungcode.demo.dto.request.IntrospectRequest;
import com.dungcode.demo.dto.request.LoginGoogleRequest;
import com.dungcode.demo.service.AuthenticationService;
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

    @PostMapping("/login-google")
    ResponseEntity<?> loginGoogle(@RequestBody @Valid LoginGoogleRequest request) {
        return (authenticationService.loginGoogle(request)).responseEntity();
    }

}
