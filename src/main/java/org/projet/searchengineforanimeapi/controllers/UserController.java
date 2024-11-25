package org.projet.searchengineforanimeapi.controllers;

import lombok.RequiredArgsConstructor;
import org.projet.searchengineforanimeapi.dtos.UserInput;
import org.projet.searchengineforanimeapi.entities.User;
import org.projet.searchengineforanimeapi.repositories.UserRepo;
import org.projet.searchengineforanimeapi.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserRepo userRepo;

    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String verificationCode = request.get("verificationCode");

        try {
            Optional<User> userOptional = userRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getVerificationCode().equals(verificationCode)) {
                    user.setVerified(true);
                    user.setVerificationCode(null);
                    userService.saveUser(user);
                    return ResponseEntity.ok("Compte vérifié avec succès.");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Code de vérification incorrect.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la vérification.");
        }
    }

    @PostMapping("/resend-code")
    public ResponseEntity<String> resendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        System.out.println("Email reçu : " + email);

        try {
            userService.resendVerificationCode(email);
            return ResponseEntity.ok("Code de vérification renvoyé avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors du renvoi du code.");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String result = userService.resetPassword(request);

        if (result.equals("Token invalide ou expiré")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String result = userService.forgotPassword(email);

        if (result.equals("Utilisateur introuvable")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserInput input) {
        try {
            User updatedUser = userService.updateUserFromInput(id, input);
            if (updatedUser != null) {
                return ResponseEntity.ok(updatedUser);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
