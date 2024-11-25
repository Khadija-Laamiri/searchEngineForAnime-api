package org.projet.searchengineforanimeapi.services;

import org.projet.searchengineforanimeapi.dtos.UserInput;
import org.projet.searchengineforanimeapi.entities.User;

import java.util.Map;
import java.util.Optional;

public interface UserService {

    User getUserById(Long id);

    Optional<User> findByResetPasswordToken(String token);

    String resetPassword(Map<String, String> request);

    String forgotPassword(String email);

    User updateUserFromInput(Long id, UserInput input);

    User saveUser(User user);

    void resendVerificationCode(String email);
}
