package com.maljunaplanedo.schooltestingsystem.service;

import com.maljunaplanedo.schooltestingsystem.dto.RegistrationFormDto;
import com.maljunaplanedo.schooltestingsystem.exception.BadRegistrationDataFormatException;
import com.maljunaplanedo.schooltestingsystem.exception.RegistrationException;
import com.maljunaplanedo.schooltestingsystem.exception.UsernameAlreadyUsedException;
import com.maljunaplanedo.schooltestingsystem.exception.WrongInviteCodeException;
import com.maljunaplanedo.schooltestingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    private boolean badCredentialFormat(String credential) {
        return credential.length() < 4 || credential.length() > 64 || !credential.matches("^[a-zA-Z0-9_.-]+$");
    }

    private void checkRegistrationDataFormat(String username, String password)
            throws BadRegistrationDataFormatException {
        if (badCredentialFormat(username) || badCredentialFormat(password)) {
            throw new BadRegistrationDataFormatException("Bad registration data format");
        }
    }

    @Transactional
    public void register(RegistrationFormDto formData) throws RegistrationException {
        String username = formData.getUsername();
        String password = formData.getPassword();
        String inviteCode = formData.getInviteCode();

        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyUsedException(String.format("Username %s is already used", username));
        }

        checkRegistrationDataFormat(username, password);

        var user = userRepository
            .findByInviteCode(inviteCode)
            .orElseThrow(() -> new WrongInviteCodeException(
                String.format("Invite code %s does not exist", inviteCode)
            ));

        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setInviteCode(null);

        userRepository.save(user);
    }
}
