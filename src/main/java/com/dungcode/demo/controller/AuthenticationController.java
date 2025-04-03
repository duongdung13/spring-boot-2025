package com.dungcode.demo.controller;

import com.dungcode.demo.dto.request.AuthenticationRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    com.dungcode.demo.serrvice.AuthenticationService authenticationService;

    public AuthenticationController(com.dungcode.demo.serrvice.AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    ResponseEntity<?> authenticate(@RequestBody @Valid AuthenticationRequest request) {
        return (authenticationService.authenticate(request)).responseEntity();
    }

//    @PostMapping("/introspect")
//    ResponseEntity<?> introspectToken(@RequestBody @Valid IntrospectRequest request) throws ParseException, JOSEException {
//        return (authenticationService.introspectToken(request)).responseEntity();
//    }

}
