package com.projectmatching.app.domain.user.service.userdetail;

import com.projectmatching.app.domain.user.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor


//시큐리티에서는 UserDetails를 이용해 유저정보를 관리함
public class UserDetailsImpl implements UserDetails {

    private Long id;
    private String oauthId;
    private Role role;
    private String email;
    private String name;
    private String pwd;


    @Builder
    public UserDetailsImpl(Long id, String oauthId, Role role, String email, String name, String pwd) {
        this.id = id;
        this.oauthId = oauthId;
        this.role = role;
        this.email = email;
        this.name = name;
        this.pwd = pwd;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}