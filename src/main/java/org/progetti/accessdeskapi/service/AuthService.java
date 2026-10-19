package org.progetti.accessdeskapi.service;

import lombok.RequiredArgsConstructor;
import org.progetti.accessdeskapi.dtos.CreateRequestUser;
import org.progetti.accessdeskapi.mapper.UserMapper;
import org.progetti.accessdeskapi.models.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public String register(CreateRequestUser requestUser) {
        String username = requestUser.username();
        String password = requestUser.password();

        String pswHash = passwordEncoder.encode(password);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        pswHash
                )
        );

        User user = userMapper.toEntity(requestUser);
        user.setPasswordHash(pswHash);
    }
}
