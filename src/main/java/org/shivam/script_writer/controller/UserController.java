package org.shivam.script_writer.controller;


import org.shivam.script_writer.dto.*;
import org.shivam.script_writer.entity.RefreshToken;
import org.shivam.script_writer.entity.User;
import org.shivam.script_writer.repo.RefreshTokenRepository;
import org.shivam.script_writer.service.*;
import org.shivam.script_writer.util.AccountStatus;
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
    private  final EmailService emailService;
    private final EmailTemplates emailTemplates;
    private final RefreshTokenRepository refreshTokenRepository ;
    private final RefreshTokenService refreshTokenService;
    private final OtpService otpService;


    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService, EmailService emailService, EmailTemplates emailTemplates, RefreshTokenRepository refreshTokenRepository, RefreshTokenService refreshTokenService, OtpService otpService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.emailTemplates = emailTemplates;
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenService = refreshTokenService;
        this.otpService = otpService;
    }


    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {

        UserResponse userResponse = userService.createUser(userRequest);

        String code = otpService.generateAndStore(userResponse.email());

        emailService.sendHtmlEmail(userResponse.email(), "Script Writer: OTP for Verification",
                emailTemplates.verificationOtp(userResponse.name(), code));

        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<LoginResponse> verify(@RequestBody VerifyRequest req) {
            if (!otpService.verify(req.email(), req.code())) {
                throw new RuntimeException("Invalid or expired OTP.");
            }
        otpService.invalidate(req.email());
        User user = userService.markEnabled(req.email());
        UserPrincipal principal = new UserPrincipal(user);
        String jwtToken = jwtService.generateToken(principal);

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user.getId());

        return ResponseEntity.ok(
                new LoginResponse(jwtToken, refreshToken.getToken(), "Bearer")
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();
        if (user.getStatus() == AccountStatus.UNVERIFIED) {
            String code = otpService.generateAndStore(user.getEmail());

            emailService.sendHtmlEmail(user.getEmail(), "Script Writer: OTP for Verification",
                    emailTemplates.verificationOtp(user.getName(), code));

            return ResponseEntity.accepted().body(
                    new VerificationRequiredResponse(
                            "EMAIL_VERIFICATION_REQUIRED",
                            "Your account is not verified. A new verification code has been sent.",
                            user.getEmail()
                    )
            );
        }
        String token = jwtService.generateToken(authentication);
        RefreshToken refreshToken =  refreshTokenService.createRefreshToken(user.getId());
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
