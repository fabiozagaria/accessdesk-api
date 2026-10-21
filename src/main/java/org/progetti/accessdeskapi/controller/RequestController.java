package org.progetti.accessdeskapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.progetti.accessdeskapi.dtos.request.CreateRequestSoftware;
import org.progetti.accessdeskapi.dtos.request.DecisionRequest;
import org.progetti.accessdeskapi.dtos.request.RequestResponse;
import org.progetti.accessdeskapi.models.request.Request;
import org.progetti.accessdeskapi.service.RequestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public Page<RequestResponse> findAllByUser(
            Authentication authentication,
            @PageableDefault(
                    size = 10,
                    sort = {"urgency", "createdAt"},
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
            ) {
        return requestService.findAllByUsername(authentication, pageable);

    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public Page<RequestResponse> findAll(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = {"urgency" , "createdAt"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return requestService.findAll(pageable);
    }

    @GetMapping("/{id}")
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

    @PatchMapping("/{id}/decision")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Void> patch(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody DecisionRequest request
    ) {
        requestService.decideRequest(request, id);
    }





}
