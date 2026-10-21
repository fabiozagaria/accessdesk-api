package org.progetti.accessdeskapi.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.progetti.accessdeskapi.dtos.register.AccessUser;
import org.progetti.accessdeskapi.dtos.register.RegisterResponse;
import org.progetti.accessdeskapi.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody AccessUser requestUser,
            HttpServletRequest request
    ) {

        var response = authService.register(requestUser);
        return ResponseEntity
                .created(URI.create(request.getRequestURI()))
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @Valid @RequestBody AccessUser request
    ) {
        authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
