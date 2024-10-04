package com.k_plus.internship.SecurityPackage;

import com.k_plus.internship.PrivilegePackage.Privilege;
import com.k_plus.internship.PrivilegePackage.PrivilegeRepository;
import com.k_plus.internship.RolePackage.Role;
import com.k_plus.internship.RolePackage.RoleRepository;
import com.k_plus.internship.UserPackage.User;
import com.k_plus.internship.UserPackage.UserRepository;
import com.k_plus.internship.UserPackage.UserService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    boolean alreadySetup = false;

    private final UserService userService;

    private final RoleRepository roleRepository;

    private final PrivilegeRepository privilegeRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {

        if (alreadySetup){
            return;
        }

        Privilege readPrivilege
                = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
                = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(
                readPrivilege, writePrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

        var adminRole = roleRepository.findByName("ROLE_ADMIN");
        if (adminRole.isEmpty()) {
            //TODO: add logs!!
            return;
        }

        User user = new User();
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setPassword(passwordEncoder.encode("testtest"));
        user.setEmail("test@gmail.com");
        user.setRoles(List.of(adminRole.get()));
        user.setEnabled(true);
        if (!userService.existsByEmail(user.getEmail())) {
            userService.saveUser(user);
        }

        alreadySetup = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

        var privilege = privilegeRepository.findByName(name);
        if (privilege.isEmpty()) {
            Privilege newPrivilege = new Privilege();
            newPrivilege.setName(name);
            privilegeRepository.save(newPrivilege);

            return newPrivilege;
        }

        return privilege.get();
    }

    @Transactional
    Role createRoleIfNotFound(
            String name, Collection<Privilege> privileges) {

        var role = roleRepository.findByName(name);
        if (role.isEmpty()) {
            Role newRole = new Role();
            newRole.setName(name);
            newRole.setPrivileges(privileges);
            roleRepository.save(newRole);

            return newRole;
        }

        return role.get();
    }
}