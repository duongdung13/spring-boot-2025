package com.dungcode.demo.controller;

import com.dungcode.demo.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloSpringController {

    @GetMapping()
    ResponseEntity<?> sayHello() {
        return (new SuccessResponse<>("Hello spring boot!")).responseEntity();
    }

}
