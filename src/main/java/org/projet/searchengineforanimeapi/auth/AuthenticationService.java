package org.projet.searchengineforanimeapi.auth;

import lombok.RequiredArgsConstructor;
import org.projet.searchengineforanimeapi.config.JwtService;
import org.projet.searchengineforanimeapi.entities.Role;
import org.projet.searchengineforanimeapi.entities.User;
import org.projet.searchengineforanimeapi.repositories.UserRepo;
import org.projet.searchengineforanimeapi.services.EmailService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        var user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();

        String verificationCode = generateVerificationCode();
        user.setVerificationCode(verificationCode);
        User savedUser = userRepo.save(user);
        var jwtToken = jwtService.generateToken(user);

        sendVerificationEmail(savedUser.getEmail(), verificationCode);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );
        var user = userRepo.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private String generateVerificationCode() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    private void sendVerificationEmail(String email, String verificationCode) {
        String subject = "Vérification de votre compte";
        String message = "Votre code de vérification est : " + verificationCode;
        emailService.sendEmail(email, subject, message);
    }
}

