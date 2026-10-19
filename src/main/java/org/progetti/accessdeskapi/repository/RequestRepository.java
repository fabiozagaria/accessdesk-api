package org.progetti.accessdeskapi.repository;

import org.progetti.accessdeskapi.models.request.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequestedBy(String username);
}
