package org.progetti.accessdeskapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.progetti.accessdeskapi.dtos.CreateRequestUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("auth/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody CreateRequestUser requestUser
    ) {




    }
}
