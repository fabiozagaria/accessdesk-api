package org.progetti.accessdeskapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.progetti.accessdeskapi.dtos.request.CreateRequestSoftware;
import org.progetti.accessdeskapi.dtos.request.RequestResponse;
import org.progetti.accessdeskapi.models.request.Request;
import org.progetti.accessdeskapi.service.RequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")

@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;


    @GetMapping("/mine")
    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
    public List<RequestResponse> findAllByUser(
            Authentication authentication
    ) {
        return requestService.findAllByUsername(authentication);

    }

    @GetMapping("/mine/{id}")
    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
    public RequestResponse findById(
            @PathVariable(name = "id") Long id,
            Authentication authentication
    )
    {
        return requestService.findByIdAndUsername(id, authentication);

    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
    public ResponseEntity<Void> save(
            Authentication authentication,
            @Valid @RequestBody CreateRequestSoftware requestSoftware
    ) {
        requestService.save(authentication, requestSoftware);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }



}
