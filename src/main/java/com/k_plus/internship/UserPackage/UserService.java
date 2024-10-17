package com.k_plus.internship.UserPackage;

import com.fasterxml.uuid.Generators;
import com.k_plus.internship.CommonPackage.CustomExceptions.InvalidUserInfoException;
import com.k_plus.internship.CommonPackage.CustomExceptions.UserNotFoundException;
import com.k_plus.internship.CommonPackage.CustomExceptions.UserRoleNotFoundException;
import com.k_plus.internship.RolePackage.RoleRepository;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Consumer;

import org.aspectj.weaver.ast.Var;

import javax.management.relation.RoleNotFoundException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }

    public UserResponseDto saveUser(UserRegisterDto registerUserRequest) {
        if (!registerUserRequest.getPassword().equals(registerUserRequest.getConfirmPassword())) {
            throw new InvalidUserInfoException("Password and confirm password does not equals");
        }

        User user = modelMapper.map(registerUserRequest, User.class);
        user.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
        user.setRoles(List.of(roleRepository.findByName("ROLE_USER").orElseThrow()));
        user.setId(Generators.timeBasedGenerator().generate());
        user.setEnabled(true);
        var savedUser = userRepository.save(user);

        return modelMapper.map(savedUser, UserResponseDto.class);
    }

    public User findUserToUserDetailsByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found: " + email));
    }

    @Transactional
    public void upgradeUserToAdmin(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId.toString()));
        user.addRole(roleRepository.findByName("ROLE_ADMIN").orElseThrow(() -> new UserRoleNotFoundException("Role not found")));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public UserResponseDto findUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email not found: " + email));

        return modelMapper.map(user, UserResponseDto.class);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User findUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("user with id: " + userId + " not found"));
    }
    public UserResponseDto findUserByIdReturningDto(UUID userId) {
        var user = findUserById(userId);

        return modelMapper.map(user, UserResponseDto.class);
    }

    public void deleteUserByUserId(UUID userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public void deleteCurrentUser(String email) {
        userRepository.deleteByEmail(email);
    }

    public UserResponseDto updateUserPartly(UpdateUserDto userDto, String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isEmpty()) {
            throw new UserNotFoundException("user with email: " + email);
        }

        var user = byEmail.get();
        updateFieldIfNotNull(userDto.getEmail(), user::setEmail);
        updateFieldIfNotNull(userDto.getFirstName(), user::setFirstName);
        updateFieldIfNotNull(userDto.getLastName(), user::setLastName);

        return modelMapper.map(userRepository.save(user), UserResponseDto.class);
    }

    public void updateUserPartly(UpdateUserDto userDto, UUID uuid) throws UserNotFoundException {
        Optional<User> userOptById = userRepository.findById(uuid);
        if (userOptById.isEmpty()) {
            throw new UserNotFoundException("user with id: " + uuid + " not found");
        }

        var user = userOptById.get();
        updateFieldIfNotNull(userDto.getEmail(), user::setEmail);
        updateFieldIfNotNull(userDto.getFirstName(), user::setFirstName);
        updateFieldIfNotNull(userDto.getLastName(), user::setLastName);
    }

    private <T> void updateFieldIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    public List<UserTableDto> getAllUsers() {
        var allUsers = userRepository.findAllUsersWithRoles();
        if (allUsers.isEmpty()) {
            throw new UserNotFoundException("There's no any users");
        }

        return allUsers
                .stream()
                .map(x -> {
                    var dto = modelMapper.map(x, UserTableDto.class);
                    dto.setRoleName(x.getRoles().stream().toList().get(0).getName());
                    return dto;
                    })
                .toList();
    }

    public Collection<User> findAll() {
        return userRepository.findAll();
    }
}
