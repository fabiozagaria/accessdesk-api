package org.progetti.accessdeskapi.controller;

import lombok.RequiredArgsConstructor;
import org.progetti.accessdeskapi.models.request.Request;
import org.progetti.accessdeskapi.service.RequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/requests")

@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;


    @GetMapping("/mine")
    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
    public List<Request> findAllByUser(
            Authentication authentication
    ) {
        return requestService.findAllByUsername(authentication);

    }



}
