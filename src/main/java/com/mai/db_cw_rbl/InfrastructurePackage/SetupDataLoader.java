package com.mai.db_cw_rbl.InfrastructurePackage;

import com.fasterxml.uuid.Generators;
import com.mai.db_cw_rbl.RolePackage.Dao.RoleDao;
import com.mai.db_cw_rbl.RolePackage.Role;
import com.mai.db_cw_rbl.UserPackage.Dao.UserDao;
import com.mai.db_cw_rbl.UserPackage.User;
import com.mai.db_cw_rbl.UserPackage.UserService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    private final UserDao userDao;

    private final AuthenticationManager authenticationManager;

    private final TransactionTemplate transactionTemplate;

    boolean alreadySetup = false;

    private final UserService userService;

    private final RoleDao roleDao;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {

        if (alreadySetup){
            return;
        }

        createRoleIfNotFound("ROLE_ADMIN");//, adminPrivileges);
        createRoleIfNotFound("ROLE_LAWYER");//, Arrays.asList(lawyerPrivilege));
        createRoleIfNotFound("ROLE_USER");//, Arrays.asList(readPrivilege));

        var adminRole = roleDao.findByName("ROLE_ADMIN");
        var userRole = roleDao.findByName("ROLE_USER");
        if (adminRole.isEmpty() || userRole.isEmpty()) {
            //TODO: add logs!!
            return;
        }

        User user = new User();
        user.setId(Generators.timeBasedEpochGenerator().generate());
        user.setFirstName("Admin");
        user.setLastName("Account");
        user.setPassword(passwordEncoder.encode("qweqweqwe"));
        user.setEmail("admin@gmail.com");
        user.setEnabled(true);
        user.setTokenExpired(false);
        user.setCreationTime(LocalDateTime.now());
        user.setModifiedTime(LocalDateTime.now());
        if (!userService.existsByEmail(user.getEmail())) {
            transactionTemplate.executeWithoutResult(status -> {
                userDao.save(user);
                roleDao.saveUserRoles(user, List.of(userRole.get(), adminRole.get()));
            });
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getEmail(),
                            "qweqweqwe"
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        alreadySetup = true;
    }

    @Transactional
    Role createRoleIfNotFound(
            String name) {

        var role = roleDao.findByName(name);
        if (role.isEmpty()) {
            Role newRole = new Role();
            newRole.setName(name);
            //newRole.setPrivileges(privileges);
            roleDao.save(newRole);

            return newRole;
        }

        return role.get();
    }
}