package org.progetti.accessdeskapi.service;

import lombok.RequiredArgsConstructor;
import org.progetti.accessdeskapi.models.request.Request;
import org.progetti.accessdeskapi.repository.RequestRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;

    public List<Request> findAllByUsername(Authentication authentication) {
        String username = authentication.getName();
        return requestRepository.findAllByRequestedBy(username);
    }
}
