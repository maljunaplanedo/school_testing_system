package com.maljunaplanedo.schooltestingsystem.service;

import com.maljunaplanedo.schooltestingsystem.dto.AddUserDto;
import com.maljunaplanedo.schooltestingsystem.dto.AddUserResponseDto;
import com.maljunaplanedo.schooltestingsystem.dto.RegistrationFormDto;
import com.maljunaplanedo.schooltestingsystem.exception.BadDataFormatException;
import com.maljunaplanedo.schooltestingsystem.exception.RegistrationException;
import com.maljunaplanedo.schooltestingsystem.exception.UsernameAlreadyUsedException;
import com.maljunaplanedo.schooltestingsystem.exception.WrongInviteCodeException;
import com.maljunaplanedo.schooltestingsystem.model.User;
import com.maljunaplanedo.schooltestingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class UserService {
    private final static String INVITE_CODE_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private final static int INVITE_CODE_LENGTH = 6;
    
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

    @Transactional
    public void register(RegistrationFormDto formData) throws RegistrationException, BadDataFormatException {
        String username = formData.getUsername();
        String password = formData.getPassword();
        String inviteCode = formData.getInviteCode();

        if (
            username == null || password == null ||
            inviteCode == null || badCredentialFormat(username) ||
            badCredentialFormat(password)
        ) {
            throw new BadDataFormatException("Bad registration data format");
        }

        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyUsedException(String.format("Username %s is already used", username));
        }

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

    private String generateInviteCode() {
        var inviteCodeBuilder = new StringBuilder();
        ThreadLocalRandom
            .current()
            .ints(INVITE_CODE_LENGTH, 0, INVITE_CODE_ALPHABET.length())
            .mapToObj(INVITE_CODE_ALPHABET::charAt)
            .forEach(inviteCodeBuilder::append);
        return inviteCodeBuilder.toString();
    }

    private String generateUnusedInviteCode() {
        String inviteCode;
        while (true) {
            inviteCode = generateInviteCode();
            if (!userRepository.existsByInviteCode(inviteCode)) {
                return inviteCode;
            }
        }
    }

    private boolean badNameFormat(String name) {
        return name.isEmpty() || name.length() > 64 || !name.matches("^[ЁёА-я ]+$");
    }

    @Transactional
    protected AddUserResponseDto addUser(String role, AddUserDto userInfo) throws BadDataFormatException {
        var firstName = userInfo.getFirstName();
        var lastName = userInfo.getLastName();

        if (firstName == null || lastName == null || badNameFormat(firstName) || badNameFormat(lastName)) {
            throw new BadDataFormatException("Bad data format");
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(role);

        var inviteCode = generateUnusedInviteCode();
        user.setInviteCode(inviteCode);

        userRepository.save(user);
        return new AddUserResponseDto(inviteCode);
    }

    public AddUserResponseDto addStudent(AddUserDto userInfo) throws BadDataFormatException {
        return addUser("STUDENT", userInfo);
    }

    public AddUserResponseDto addTeacher(AddUserDto userInfo) throws BadDataFormatException {
        return addUser("TEACHER", userInfo);
    }

    @Transactional
    protected void removeUser(String role, long id) throws BadDataFormatException {
        var user = userRepository
            .findById(id)
            .orElseThrow(() -> new BadDataFormatException("User does not exist"));
        if (!user.getRole().equals(role)) {
            throw new BadDataFormatException(String.format("The role of this user is not %s", role));
        }
        userRepository.delete(user);
    }

    public void removeStudent(long id) throws BadDataFormatException {
        removeUser("STUDENT", id);
    }

    public void removeTeacher(long id) throws BadDataFormatException {
        removeUser("TEACHER", id);
    }
}
