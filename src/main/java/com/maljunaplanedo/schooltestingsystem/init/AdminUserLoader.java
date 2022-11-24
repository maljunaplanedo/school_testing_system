package com.maljunaplanedo.schooltestingsystem.init;

import com.maljunaplanedo.schooltestingsystem.model.User;
import com.maljunaplanedo.schooltestingsystem.model.UserRole;
import com.maljunaplanedo.schooltestingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserLoader implements ApplicationRunner {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Value("${com.maljunaplanedo.schooltestingsystem.security.adminUsername}")
    private String ADMIN_USERNAME;

    @Value("${com.maljunaplanedo.schooltestingsystem.security.adminPassword}")
    private String ADMIN_PASSWORD;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        User admin = userRepository.findByUsername(ADMIN_USERNAME).orElse(new User());

        admin.setUsername(ADMIN_USERNAME);
        admin.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
        admin.setRole(UserRole.ADMIN);
        userRepository.save(admin);
    }
}
