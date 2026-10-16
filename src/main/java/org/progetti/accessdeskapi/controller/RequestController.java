package org.progetti.accessdeskapi.controller;

import lombok.RequiredArgsConstructor;
import org.progetti.accessdeskapi.service.RequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/requests")

@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    //GET
    /*
    @GetMapping("/mine")
    @PreAuthorize("hasAuthorities('ROLE_EMPLOYEE')")
    public ResponseEntity<?> findAll(
            Authentication authentication
    ) {
          //DA QUI
    }

     */

}
