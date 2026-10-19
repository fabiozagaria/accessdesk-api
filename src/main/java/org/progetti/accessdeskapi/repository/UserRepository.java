package org.progetti.accessdeskapi.repository;

import org.progetti.accessdeskapi.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String username);
}
