package org.progetti.accessdeskapi.service;

import lombok.RequiredArgsConstructor;
import org.progetti.accessdeskapi.exception.NotFoundUserException;
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
    private final UserService userService;

    public List<Request> findAllByUsername(Authentication authentication) {
        String username = authentication.getName();
        var user = userService.findByUsername(username)
                .orElseThrow(()-> new NotFoundUserException("Not Found"));
        return requestRepository.findAllByRequestedBy(user);
    }
}
