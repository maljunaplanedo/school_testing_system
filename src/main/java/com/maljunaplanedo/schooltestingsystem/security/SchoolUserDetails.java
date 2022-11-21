package com.maljunaplanedo.schooltestingsystem.security;

import com.maljunaplanedo.schooltestingsystem.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class SchoolUserDetails implements UserDetails {

    private String username;
    private String password;
    private GrantedAuthority authority;

    public SchoolUserDetails(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authority = new SimpleGrantedAuthority(user.getRole());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(authority);
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
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
