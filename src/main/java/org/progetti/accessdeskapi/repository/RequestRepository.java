package org.progetti.accessdeskapi.repository;

import org.progetti.accessdeskapi.models.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
