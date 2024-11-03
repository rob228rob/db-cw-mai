package com.mai.db_cw_rbl.InfrastructurePackage.UserDetails;

import com.mai.db_cw_rbl.RolePackage.Dao.RoleDao;
import com.mai.db_cw_rbl.RolePackage.Role;
import com.mai.db_cw_rbl.UserPackage.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    private final MessageSource messages;

    private final RoleDao roleDao;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        var user = userService.findUserByEmailForUserDetails(email);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                getAuthorities(roleDao.findRolesByUserId(user.getId())));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<Role> roles) {

        return getGrantedAuthorities(roles.stream().map(Role::getName).toList());
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        return privileges.stream()
                .map(x -> {
                    return (GrantedAuthority) new SimpleGrantedAuthority(x);
                })
                .toList();
    }

}
