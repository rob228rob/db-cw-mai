package com.mai.db_cw_rbl.InfrastructurePackage.Auth;

import com.mai.db_cw_rbl.InfrastructurePackage.CommonPackage.CustomExceptions.UserAlreadyExistException;
import com.mai.db_cw_rbl.InfrastructurePackage.CommonPackage.ErrorResponseDto;
import com.mai.db_cw_rbl.InfrastructurePackage.CommonPackage.LoginUserDto;
import com.mai.db_cw_rbl.InfrastructurePackage.UserDetails.CustomUserDetailsService;
import com.mai.db_cw_rbl.LawyerPackage.Dtos.LawyerCreationRequest;
import com.mai.db_cw_rbl.LawyerPackage.Dtos.LawyerResponse;
import com.mai.db_cw_rbl.LawyerPackage.LawyerService;
import com.mai.db_cw_rbl.UserPackage.UserRegistrationRequest;
import com.mai.db_cw_rbl.UserPackage.UserResponse;
import com.mai.db_cw_rbl.UserPackage.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final CustomUserDetailsService userDetailsService;

    private final UserService userService;

    private final AuthenticationManager authenticationManager;
    private final LawyerService lawyerService;

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

//    @PostMapping("/register-lawyer")
//    public ResponseEntity<LawyerResponseDto>

    @PostMapping("/register/lawyer")
    public ResponseEntity<LawyerResponse> registerLawyer(
            @Valid @RequestBody LawyerCreationRequest lawyerRegistrationRequest, HttpServletRequest request) {
        //TODO: do not recommend, bad SRC principle! move method to service layer
        throwIfUserAlreadyExists(lawyerRegistrationRequest.getUserData());

        LawyerResponse response = lawyerService.saveLawyer(lawyerRegistrationRequest);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        lawyerRegistrationRequest.getUserData().getEmail(),
                        lawyerRegistrationRequest.getUserData().getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @Valid
            @RequestBody
            UserRegistrationRequest userRegistrationRequest,
            HttpServletRequest request) {
        //TODO: do not recommend, bad SRC principle! move method to service layer
        throwIfUserAlreadyExists(userRegistrationRequest);

        try {
            UserResponse userResponse = userService.saveUser(userRegistrationRequest);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userRegistrationRequest.getEmail(),
                            userRegistrationRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }
    }

    private void throwIfUserAlreadyExists(@Valid UserRegistrationRequest userRegistrationRequest) {
        if (userService.existsByEmail(userRegistrationRequest.getEmail())) {
            throw new UserAlreadyExistException("User with email: " + userRegistrationRequest.getEmail());
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

            return ResponseEntity.ok().build();
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }
}