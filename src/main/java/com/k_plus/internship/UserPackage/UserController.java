package com.k_plus.internship.UserPackage;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.k_plus.internship.CommonPackage.CustomExceptions.UserNotFoundException;
import com.k_plus.internship.CommonPackage.ErrorResponseDto;
import com.k_plus.internship.RolePackage.RoleRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final RoleRepository roleRepository;

    private final UserService userService;

    @PreAuthorize("hasRole(\"ADMIN\")")
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getUserById(@PathVariable UUID id) {
        try {
            UserResponseDto userById = userService.findUserById(id);

            return new ResponseEntity<>(userById, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponseDto(e.getMessage(), 404), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get/current")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        String email = principal.getName();
        var userByEmail = userService.findUserByEmail(email);
        if (userByEmail.isEmpty()) {
            return new ResponseEntity<>(new ErrorResponseDto("User request with email: " + email + " not found", 404), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(userByEmail, HttpStatus.OK);
    }

    @PreAuthorize("hasRole(\"ROLE_ADMIN\")")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUserByUserId(@PathVariable UUID id) {
        userService.deleteUserByUserId(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/current")
    public ResponseEntity<?> deleteCurrentUser(Principal principal) {
        userService.deleteCurrentUser(principal.getName());

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole(\"ROLE_ADMIN\", \"ROLE_EDITOR\")")
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateUserByUserId(@RequestBody @Valid UpdateUserDto updateUserDto, @PathVariable UUID id) {
        try {
            userService.updateUserPartly(updateUserDto, id);

            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(
                    new ErrorResponseDto(e.getMessage(), 404), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/update/current")
    public ResponseEntity<UserResponseDto> updateCurrentUser(@RequestBody @Valid UpdateUserDto userDto, Principal principal) {
        String email = principal.getName();
        UserResponseDto userResponseDto = userService.updateUserPartly(userDto, email);
        return ResponseEntity.ok().body(userResponseDto);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<UserTableDto>> getAllUsers() {
        return ResponseEntity
                .ok(userService.getAllUsers());
    }

}
