package com.maljunaplanedo.schooltestingsystem.service;

import com.maljunaplanedo.schooltestingsystem.service.dto.ClassDto;
import com.maljunaplanedo.schooltestingsystem.service.dto.UserDto;
import com.maljunaplanedo.schooltestingsystem.service.dto.RegistrationFormDto;
import com.maljunaplanedo.schooltestingsystem.exception.BadDataFormatException;
import com.maljunaplanedo.schooltestingsystem.exception.RegistrationException;
import com.maljunaplanedo.schooltestingsystem.exception.UsernameAlreadyUsedException;
import com.maljunaplanedo.schooltestingsystem.exception.WrongInviteCodeException;
import com.maljunaplanedo.schooltestingsystem.model.User;
import com.maljunaplanedo.schooltestingsystem.model.UserRole;
import com.maljunaplanedo.schooltestingsystem.repository.ClassRepository;
import com.maljunaplanedo.schooltestingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
public class UserService {
    private final static String INVITE_CODE_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private final static int INVITE_CODE_LENGTH = 6;

    private UserRepository userRepository;

    private ClassService classService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    void setClassRepository(ClassService classService) {
        this.classService = classService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    private boolean badCredentialFormat(@Nullable String credential) {
        return credential == null || credential.length() < 4 || credential.length() > 64
            || !credential.matches("^[a-zA-Z0-9_.-]+$");
    }

    public void register(RegistrationFormDto formData) throws RegistrationException, BadDataFormatException {
        String username = formData.getUsername();
        String password = formData.getPassword();
        String inviteCode = formData.getInviteCode();

        if (
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

    private boolean badNameFormat(@Nullable String name) {
        return name == null || name.isEmpty() || name.length() > 64 || !name.matches("^[ЁёА-я ]+$");
    }

    private User findById(long id) throws BadDataFormatException {
        return userRepository
            .findById(id)
            .orElseThrow(() -> new BadDataFormatException("User does not exist"));
    }

    private User findByIdAndCheckRole(long id, UserRole role) throws BadDataFormatException {
        var user = findById(id);
        if (!user.getRole().equals(role)) {
            throw new BadDataFormatException(String.format("The role of this user is not %s", role));
        }
        return user;
    }

    public Optional<User> currentUser() {
        var currentAuthentication = SecurityContextHolder.getContext().getAuthentication();
        if (currentAuthentication == null) {
            return Optional.empty();
        }
        return userRepository.findByUsername(currentAuthentication.getName());
    }

    public User currentUserSafe() throws BadDataFormatException {
        return currentUser().orElseThrow(() -> new BadDataFormatException("No one is logged in"));
    }

    public List<UserDto> getAllUsers(UserRole role) {
        return userRepository
            .findAllByRole(role)
            .stream()
            .map(UserDto::brief)
            .toList();
    }

    public List<UserDto> getAllStudents() {
        return getAllUsers(UserRole.STUDENT);
    }

    public List<UserDto> getAllTeachers() {
        return getAllUsers(UserRole.TEACHER);
    }

    private UserDto getUser(long id, UserRole role) throws BadDataFormatException {
        var user = findByIdAndCheckRole(id, role);
        return UserDto.full(user);
    }

    public UserDto getStudent(long id) throws BadDataFormatException {
        return getUser(id, UserRole.STUDENT);
    }

    public UserDto getCurrentStudent() throws BadDataFormatException {
        var user = currentUserSafe();
        return getStudent(user.getId());
    }

    public UserDto getTeacher(long id) throws BadDataFormatException {
        return getUser(id, UserRole.TEACHER);
    }

    private boolean badClass(@Nullable ClassDto classInfo) throws BadDataFormatException {
        return classInfo == null || classInfo.getId() == null;
    }

    protected void saveUser(User user, UserDto userInfo) throws BadDataFormatException {
        var firstName = userInfo.getFirstName();
        var lastName = userInfo.getLastName();

        if (badNameFormat(firstName) || badNameFormat(lastName)) {
            throw new BadDataFormatException("Bad data format");
        }

        if (user.getRole().equals(UserRole.STUDENT)) {
            var classInfo = userInfo.getSchoolClass();
            if (badClass(classInfo)) {
                throw new BadDataFormatException("Bad data format");
            }

            var schoolClass = classService.findById(classInfo.getId());
            user.setSchoolClass(schoolClass);
        }

        user.setFirstName(firstName);
        user.setLastName(lastName);

        userRepository.save(user);
    }

    protected String addUser(UserRole role, UserDto userInfo) throws BadDataFormatException {
        var user = new User();
        user.setRole(role);

        var inviteCode = generateUnusedInviteCode();
        user.setInviteCode(inviteCode);

        saveUser(user, userInfo);
        return inviteCode;
    }

    public String addStudent(UserDto userInfo) throws BadDataFormatException {
        return addUser(UserRole.STUDENT, userInfo);
    }

    public String addTeacher(UserDto userInfo) throws BadDataFormatException {
        return addUser(UserRole.TEACHER, userInfo);
    }

    protected void updateUser(long id, UserRole role, UserDto userInfo) throws BadDataFormatException {
        saveUser(findByIdAndCheckRole(id, role), userInfo);
    }

    public void updateStudent(long id, UserDto userInfo) throws BadDataFormatException {
        updateUser(id, UserRole.STUDENT, userInfo);
    }

    public void updateTeacher(long id, UserDto userInfo) throws BadDataFormatException {
        updateUser(id, UserRole.TEACHER, userInfo);
    }

    protected void removeUser(long id, UserRole role) throws BadDataFormatException {
        userRepository.delete(findByIdAndCheckRole(id, role));
    }

    public void removeStudent(long id) throws BadDataFormatException {
        removeUser(id, UserRole.STUDENT);
    }

    public void removeTeacher(long id) throws BadDataFormatException {
        removeUser(id, UserRole.TEACHER);
    }
}
