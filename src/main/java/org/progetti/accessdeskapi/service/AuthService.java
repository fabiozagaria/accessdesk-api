package org.progetti.accessdeskapi.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.hibernate.exception.ConstraintViolationException;
import org.progetti.accessdeskapi.dtos.register.AccessUser;
import org.progetti.accessdeskapi.dtos.register.RegisterResponse;
import org.progetti.accessdeskapi.exception.ex.UserAlreadyExistsException;
import org.progetti.accessdeskapi.mapper.user.UserMapper;
import org.progetti.accessdeskapi.models.user.Role;
import org.progetti.accessdeskapi.models.user.User;
import org.progetti.accessdeskapi.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Transactional
    public RegisterResponse register(@Valid AccessUser requestUser) {
        String username = requestUser.username();
        String pswHash = passwordEncoder.encode(requestUser.password());

        User user = userMapper.toEntity(requestUser);
        user.setUsername(username);
        user.setPasswordHash(pswHash);
        user.setRole(Role.EMPLOYEE);

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            Throwable cause = e;
            while (cause != null) {
                if (cause instanceof ConstraintViolationException violation
                        && "uk_users_username".equals(violation.getConstraintName())) {
                    throw new UserAlreadyExistsException("Username occupato");
                }
                cause = cause.getCause();
            }
            throw e;
        }
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
