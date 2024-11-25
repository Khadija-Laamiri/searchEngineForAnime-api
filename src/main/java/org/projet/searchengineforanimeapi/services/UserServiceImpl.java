package org.projet.searchengineforanimeapi.services;


import lombok.RequiredArgsConstructor;
import org.projet.searchengineforanimeapi.auth.AuthenticationService;
import org.projet.searchengineforanimeapi.dtos.UserInput;
import org.projet.searchengineforanimeapi.entities.User;
import org.projet.searchengineforanimeapi.repositories.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuthenticationService authenticationService;


    @Override
    public User getUserById(Long id) {
       return userRepo.findById(id).orElse(null);
    }

    @Override
    public Optional<User> findByResetPasswordToken(String token) {
        return userRepo.findByResetPasswordToken(token);
    }

    @Override
    public String resetPassword(Map<String, String> request) {
        String token = request.get("token");
        String password = request.get("password");

        Optional<User> userOpt = findByResetPasswordToken(token);
        if (userOpt.isEmpty()) {
            return "Token invalide ou expiré";
        }

        User user = userOpt.get();

        if (user.getResetPasswordTokenExpiry().before(new Date())) {
            return "Token invalide ou expiré";
        }

        user.setPassword(passwordEncoder.encode(password));
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpiry(null);
        userRepo.save(user);

        return "Mot de passe réinitialisé avec succès";
    }

    @Override
    public String forgotPassword(String email) {
        User user = userRepo.findByEmail(email).orElse(null);
        if (user == null) {
            return "Utilisateur introuvable";
        }

//        String token = UUID.randomUUID().toString();
//        user.setResetPasswordToken(token);
//        user.setResetPasswordTokenExpiry(new Date(System.currentTimeMillis() + 3600000)); // 1 hour
//        userRepo.save(user);

        String resetLink = "http://localhost:5173/reset-password" ;

        String subject = "Réinitialisation de votre mot de passe";
        String text = "Bonjour,\n\nCliquez sur le lien suivant pour réinitialiser votre mot de passe :\n" + resetLink +
                "\n\nSi vous n'avez pas demandé cette réinitialisation, veuillez ignorer cet e-mail.";
        emailService.sendEmail(user.getEmail(), subject, text);

        return "Lien de réinitialisation envoyé";
    }

    public User updateUserFromInput(Long id, UserInput input) {
        Optional<User> existingUserOpt = userRepo.findById(id);

        if (existingUserOpt.isPresent()) {
            User user = existingUserOpt.get();

            if (input.getName() != null) user.setName(input.getName());
            if (input.getEmail() != null) user.setEmail(input.getEmail());

            return userRepo.save(user);
        }

        return null;
    }

    @Override
    public User saveUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public void resendVerificationCode(String email) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé."));

        String verificationCode = authenticationService.generateVerificationCode();
        user.setVerificationCode(verificationCode);
        userRepo.save(user);
        authenticationService.sendVerificationEmail(user.getEmail(), verificationCode);
    }
}
