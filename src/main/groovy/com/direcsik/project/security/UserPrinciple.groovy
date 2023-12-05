package com.direcsik.project.security

import com.direcsik.project.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

import java.util.stream.Collectors

class UserPrinciple implements UserDetails {

    private String id

    private String username

    private String password

    private Collection<? extends GrantedAuthority> authorities

    UserPrinciple(User user) {
        this.id = user.id
        this.username = user.username
        this.password = user.password
        this.authorities = getAuthoritiesInternal(user)
    }

    String getId() {
        return id
    }

    @Override
    Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities
    }

    @Override
    String getPassword() {
        return password
    }

    @Override
    String getUsername() {
        return username
    }

    @Override
    boolean isAccountNonExpired() {
        return true
    }

    @Override
    boolean isAccountNonLocked() {
        return true
    }

    @Override
    boolean isCredentialsNonExpired() {
        return true
    }

    @Override
    boolean isEnabled() {
        return true
    }

    private Collection<? extends GrantedAuthority> getAuthoritiesInternal(User user) {
        return user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList())
    }
}
