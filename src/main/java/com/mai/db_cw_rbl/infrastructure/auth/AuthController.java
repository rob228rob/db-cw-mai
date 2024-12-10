package com.mai.db_cw_rbl.infrastructure.auth;

import com.mai.db_cw_rbl.infrastructure.auth.dto.AuthRequest;
import com.mai.db_cw_rbl.infrastructure.user_details.CustomUserDetailsService;
import com.mai.db_cw_rbl.infrastructure.auth.dto.AuthResponse;
import com.mai.db_cw_rbl.infrastructure.auth.dto.RefreshTokenRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Неверный логин или пароль");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.username());
        final String jwt = jwtUtil.generateToken(userDetails, 1000 * 60 * 60); // токен на 1 час
        final String refreshJwt = jwtUtil.generateToken(userDetails, 1000 * 60 * 60 * 24 * 7); // рефреш токен на неделю

        return ResponseEntity.ok(new AuthResponse(jwt, refreshJwt));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAuthenticationToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.refreshToken();
        if (jwtUtil.isTokenExpired(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token expired");
        }

        final String username = jwtUtil.extractUsername(refreshToken);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtUtil.validateToken(refreshToken, userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        final String jwt = jwtUtil.generateToken(userDetails, 1000 * 60 * 60);
        return ResponseEntity.ok(new AuthResponse(jwt, refreshToken));
    }
}
