package com.mai.db_cw_rbl.UserPackage;

import java.security.Principal;
import java.util.UUID;

import com.mai.db_cw_rbl.UserPackage.Dto.UpdateUserDto;
import com.mai.db_cw_rbl.UserPackage.Dto.UserResponse;
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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/is-lawyer")
    public ResponseEntity<Boolean> isLawyer(Principal principal) {
        return ResponseEntity.ok().body(userService.hasRoleLawyer(principal.getName()));
    }

    @PreAuthorize("hasRole(\"ADMIN\")")
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getUserById(@PathVariable UUID id) {
        var userById = userService.findUserByIdReturningDto(id);

        return new ResponseEntity<>(userById, HttpStatus.OK);
    }

    @GetMapping("/get/current")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        String email = principal.getName();

        return ResponseEntity.ok(userService.findUserByEmail(email));
    }

    @PreAuthorize("hasRole(\"ADMIN\")")
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
        userService.updateUserPartly(updateUserDto, id);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update/current")
    public ResponseEntity<UserResponse> updateCurrentUser(@RequestBody @Valid UpdateUserDto userDto, Principal principal) {
        String email = principal.getName();
        UserResponse userResponse = userService.updateUserPartly(userDto, email);
        return ResponseEntity.ok().body(userResponse);
    }
}
