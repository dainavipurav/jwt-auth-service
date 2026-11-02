
package com.learning.springboot.authservice.security;

import com.learning.springboot.authservice.entity.UserEntity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final Long userId;
    private final String username;
    private final String password;
    private final Set<GrantedAuthority> authorities;

    private final boolean enabled;

    public CustomUserDetails(UserEntity user) {

        this.userId = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();

        this.enabled = user.isEnabled();

        // Load and convert roles while the transaction is active
        this.authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().startsWith("ROLE_")
                        ? role.getRole()
                        : "ROLE_" + role.getRole()))
                .collect(Collectors.toSet());
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
