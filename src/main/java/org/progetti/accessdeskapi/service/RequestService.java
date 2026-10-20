package org.progetti.accessdeskapi.service;

import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.progetti.accessdeskapi.dtos.request.CreateRequestSoftware;
import org.progetti.accessdeskapi.dtos.request.RequestResponse;
import org.progetti.accessdeskapi.exception.ex.NotFoundRequestException;
import org.progetti.accessdeskapi.exception.ex.NotFoundUserException;
import org.progetti.accessdeskapi.mapper.request.RequestMapper;
import org.progetti.accessdeskapi.models.request.StatusRequest;
import org.progetti.accessdeskapi.models.user.User;
import org.progetti.accessdeskapi.repository.RequestRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserService userService;
    private final RequestMapper requestMapper;
    private final EntityManager entityManager;

    public List<RequestResponse> findAllByUsername(Authentication authentication) {
        String username = authentication.getName();
        var user = userService.findByUsername(username)
                .orElseThrow(()-> new NotFoundUserException(String.format(
                        "%s non trovato", username
                )));
        var requests = requestRepository.findAllByRequestedBy(user);
        return requests
                .stream()
                .map(requestMapper::toDTO)
                .toList();

    }

    public RequestResponse findByIdAndUsername(Long id, Authentication authentication) {
        var user = findUser(authentication);
        var request = requestRepository.findByIdAndRequestedBy(id, user)
                .orElseThrow(()-> new NotFoundRequestException(
                        String.format(
                                "%s not found", id
                        )
                ));
        return requestMapper.toDTO(request);

    }

    @Transactional
    public void save(
            Authentication authentication,
            @Valid CreateRequestSoftware requestSoftware
    ) {
        var user = findUser(authentication);
        var request = requestMapper.toEntity(requestSoftware);
        entityManager.persist(request);
        request.setRequestedBy(user);
        request.setCreatedAt(Instant.now());
        request.setDecidedAt(null);
        request.setStatus(StatusRequest.PENDING);
        request.setVersion(0);
    }

    private User findUser(Authentication authentication) {
        var username = authentication.getName();
        return userService.findByUsername(username)
                .orElseThrow(()-> new NotFoundUserException(String.format(
                        "%s not found", username
                )));
    }


}
