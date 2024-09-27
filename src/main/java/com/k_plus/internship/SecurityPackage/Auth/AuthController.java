package com.k_plus.internship.SecurityPackage.Auth;

import com.k_plus.internship.CommonPackage.ErrorResponseDto;
import com.k_plus.internship.CommonPackage.LoginUserDto;
import com.k_plus.internship.SecurityPackage.UserDetails.CustomUserDetailsService;
import com.k_plus.internship.UserPackage.RegisterUserRequest;
import com.k_plus.internship.UserPackage.ResponseUserDto;
import com.k_plus.internship.UserPackage.UserService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final CustomUserDetailsService userDetailsService;

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    @GetMapping
    public String healthCheck() {
        return "OK";
    }
//    @Operation(summary = "Регистрация нового пользователя", description = "Создает нового пользователя в системе. Требуются логин, пароль и подтверждение пароля.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован",
//                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomUser.class))),
//            @ApiResponse(responseCode = "409", description = "Пользователь с таким логином уже существует",
//                    content = @Content(mediaType = "application/json")),
//            @ApiResponse(responseCode = "401", description = "Неверные данные пользователя",
//                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
//    })
    @PostMapping("/register")
    public ResponseEntity<?> registerTeacher(
            @RequestBody @Valid RegisterUserRequest registerTeacherDTO) {
        if (userService.existsByEmail(registerTeacherDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with this login already exists");
        }

        try {
            ResponseUserDto newTeacher = userService.saveUser(registerTeacherDTO);
            return ResponseEntity.ok(newTeacher);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }
    }

//    @Operation(summary = "Авторизация пользователя", description = "Авторизация пользователя с использованием логина и пароля.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Успешная авторизация"),
//            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
//                    content = @Content(mediaType = "application/json")),
//            @ApiResponse(responseCode = "401", description = "Неверный логин или пароль",
//                    content = @Content(mediaType = "application/json"))
//    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginUserDto loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            var user = userService.findUserByEmail(loginRequest.getEmail());

            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with this email does not exist: " + loginRequest.getEmail());
            }

            return ResponseEntity.ok().build();
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }
}