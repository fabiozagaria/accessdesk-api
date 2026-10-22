package org.progetti.accessdeskapi.service;

import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.progetti.accessdeskapi.dtos.request.CreateRequestSoftware;
import org.progetti.accessdeskapi.dtos.request.Decision;
import org.progetti.accessdeskapi.dtos.request.DecisionRequest;
import org.progetti.accessdeskapi.dtos.request.RequestResponse;
import org.progetti.accessdeskapi.exception.ex.NotFoundRequestException;
import org.progetti.accessdeskapi.exception.ex.NotFoundUserException;
import org.progetti.accessdeskapi.mapper.request.RequestMapper;
import org.progetti.accessdeskapi.models.request.StatusRequest;
import org.progetti.accessdeskapi.models.user.User;
import org.progetti.accessdeskapi.repository.RequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserService userService;
    private final RequestMapper requestMapper;
    private final EntityManager entityManager;

    public Page<RequestResponse> findAllByUsername(Authentication authentication,
                                                   Pageable pageable) {
        var user = findUser(authentication);
        var requests = requestRepository.findAllByRequestedBy(user, pageable);
        return requests.map(requestMapper::toDTO);

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
        request.setRequestedBy(user);
        request.setCreatedAt(Instant.now());
        request.setDecidedAt(null);
        request.setStatus(StatusRequest.PENDING);

        entityManager.persist(request);

    }


    public Page<RequestResponse> findAll(Pageable pageable) {
        var requestsEntities = requestRepository.findAll(pageable);
        return requestsEntities.map(requestMapper::toDTO);
    }

@PreAuthorize("hasAuthority('ROLE_MANAGER')")
@Transactional
    public void decideRequest(DecisionRequest decision, Long id) {
        var request = requestRepository.findById(id)
                .orElseThrow( ()-> new NotFoundRequestException(
                        String.format(
                                "%s non trovato", id
                        )
                ));
        if(request.getStatus() != StatusRequest.PENDING) {
           
             throw new RequestAlreadyDecidedException("Richiesta gia gestita");
        }
request.setStatus(decision.decision().name());
request.setDecidedAt(Instant.now());

    }

    private User findUser(Authentication authentication) {
        var username = authentication.getName();
        return userService.findByUsername(username)
                .orElseThrow(()-> new NotFoundUserException(String.format(
                        "%s not found", username
                )));
    }


}
