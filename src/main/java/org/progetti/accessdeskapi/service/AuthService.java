package org.progetti.accessdeskapi.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.progetti.accessdeskapi.dtos.register.AccessUser;
import org.progetti.accessdeskapi.dtos.register.RegisterResponse;
import org.progetti.accessdeskapi.mapper.user.UserMapper;
import org.progetti.accessdeskapi.models.user.Role;
import org.progetti.accessdeskapi.models.user.User;
import org.progetti.accessdeskapi.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public RegisterResponse register(@Valid AccessUser requestUser) {
        String username = requestUser.username();
        String pswHash = passwordEncoder.encode(requestUser.password());

        User user = userMapper.toEntity(requestUser);
        user.setUsername(username);
        user.setPasswordHash(pswHash);
        user.setRole(Role.EMPLOYEE);

        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    public void login(@Valid AccessUser accessUser) {
        authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(
                        accessUser.username(),
                        accessUser.password()
                )
        );


    }


    private void convertRole(User user) {
        if(user.getRole().name().equals(String.valueOf(Role.EMPLOYEE))) {
            user.setRole(Role.MANAGER);
        } else {
            user.setRole(Role.EMPLOYEE);
        }
    }


}
