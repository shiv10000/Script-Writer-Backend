package org.shivam.script_writer.controller;


import org.shivam.script_writer.dto.LoginResponse;
import org.shivam.script_writer.dto.LogoutResponse;
import org.shivam.script_writer.dto.RefreshTokenRequest;
import org.shivam.script_writer.dto.RefreshTokenResponse;
import org.shivam.script_writer.dto.UserRequest;
import org.shivam.script_writer.dto.UserResponse;
import org.shivam.script_writer.entity.RefreshToken;
import org.shivam.script_writer.repo.RefreshTokenRepository;
import org.shivam.script_writer.service.JwtService;
import org.shivam.script_writer.dto.LoginRequest;
import org.shivam.script_writer.service.RefreshTokenService;
import org.shivam.script_writer.service.UserPrincipal;
import org.shivam.script_writer.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository ;
    private final RefreshTokenService refreshTokenService;

    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService, RefreshTokenRepository refreshTokenRepository, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {

        UserResponse userResponse = userService.createUser(userRequest);

        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        String token = jwtService.generateToken(authentication);

        RefreshToken refreshToken =  refreshTokenService.createRefreshToken(principal.getUser().getId());
        return ResponseEntity.ok(new LoginResponse(token,refreshToken.getToken(), "Bearer"));
    }


    @DeleteMapping("/user/{id}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable Long id) {

        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.updateUser(id, userRequest));

    }

    @GetMapping("/user")
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("user/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        String requestToken = request.refreshToken();


        return refreshTokenRepository.findByToken(requestToken)
                .map(token -> {
                    if (refreshTokenService.isTokenExpired(token)) {
                        refreshTokenRepository.delete(token);
                        return ResponseEntity.badRequest().body("Refresh token expired. Please login again.");
                    }

                    UserDetails userDetails = new UserPrincipal(token.getUser());

                    String newJwt = jwtService.generateToken(userDetails);

                    return ResponseEntity.ok(new RefreshTokenResponse(newJwt, "Bearer"));
                })
                .orElse(ResponseEntity.badRequest().body("Invalid refresh token."));
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(@RequestBody RefreshTokenRequest request) {
        String requestToken = request.refreshToken();

        refreshTokenRepository.findByToken(requestToken)
                .ifPresent(refreshTokenRepository::delete);

        return ResponseEntity.ok(new LogoutResponse("Logged out successfully."));
    }

}
