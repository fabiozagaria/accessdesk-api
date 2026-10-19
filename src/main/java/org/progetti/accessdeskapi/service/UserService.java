package org.progetti.accessdeskapi.service;

import lombok.RequiredArgsConstructor;
import org.progetti.accessdeskapi.models.user.User;
import org.progetti.accessdeskapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
