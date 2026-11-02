
package com.learning.springboot.authservice.security;

import com.learning.springboot.authservice.entity.UserEntity;
import com.learning.springboot.authservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found: " + username
                        )
                );

        return User.withUsername(userEntity.getUsername())
                .password(userEntity.getPassword())
                .authorities(userEntity.getRoles()
                        .stream().map(
                                roleEntity -> new SimpleGrantedAuthority(
                                        roleEntity.getRole().startsWith("ROLE_")
                                                ?  roleEntity.getRole()
                                                : "ROLE_" + roleEntity.getRole()
                                        )
                                ).collect(Collectors.toSet())
                ).build();

    }
}

