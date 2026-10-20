package org.progetti.accessdeskapi.security;

import lombok.RequiredArgsConstructor;
import org.progetti.accessdeskapi.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var user = userRepository.findByUsername(username)
                .orElseThrow( ()-> new UsernameNotFoundException("Not found"));

        List<GrantedAuthority> grantedAuthorities =
                user.getRole()
                        .getPermissions()
                        .stream()
                        .map( authorities -> (GrantedAuthority)
                                new SimpleGrantedAuthority(authorities.name())
                        )
                        .toList();

        GrantedAuthority role = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

        return org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(user.getPasswordHash())
                .roles(String.valueOf(role))
                .authorities(grantedAuthorities)
                .build();
    }
}
