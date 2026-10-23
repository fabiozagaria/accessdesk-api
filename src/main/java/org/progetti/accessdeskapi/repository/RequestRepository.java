package org.progetti.accessdeskapi.repository;

import org.progetti.accessdeskapi.models.request.Request;
import org.progetti.accessdeskapi.models.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    @EntityGraph(attributePaths = "requestedBy")
    Page<Request> findAllByRequestedBy(User user, Pageable pageable);

    @EntityGraph(attributePaths = "requestedBy")
    Optional<Request> findByIdAndRequestedBy(Long id, User user);

    @Override
    @EntityGraph(attributePaths = "requestedBy")
    Page<Request> findAll(Pageable pageable);
}
