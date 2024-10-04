package com.k_plus.internship.UserPackage;

import com.fasterxml.uuid.Generators;
import com.k_plus.internship.CommonPackage.CustomExceptions.InvalidUserInfoException;
import com.k_plus.internship.CommonPackage.CustomExceptions.UserNotFoundException;
import com.k_plus.internship.RolePackage.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setId(Generators.timeBasedEpochGenerator().generate());;
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

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserResponseDto findUserById(UUID userId) {
        var user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("user with id: " + userId + " not found"));

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

    public List<UserResponseDto> getAllUsers() {
        List<User> allUsers = userRepository.findAllUsers();
        if (allUsers.isEmpty()) {
            throw new UserNotFoundException("There's no any users");
        }

        return allUsers
                .stream()
                .map(x -> modelMapper.map(x, UserResponseDto.class))
                .toList();
    }
}
