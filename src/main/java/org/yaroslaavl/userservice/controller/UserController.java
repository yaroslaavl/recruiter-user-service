package org.yaroslaavl.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @GetMapping("/test-1")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("hello test-1");
    }

    @GetMapping("/test-2")
    public ResponseEntity<String> test2() {
        return ResponseEntity.ok("hello test-2");
    }

}
