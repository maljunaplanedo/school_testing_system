package com.maljunaplanedo.schooltestingsystem.repository;

import com.maljunaplanedo.schooltestingsystem.model.User;
import com.maljunaplanedo.schooltestingsystem.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByInviteCode(String inviteCode);

    boolean existsByUsername(String username);

    boolean existsByInviteCode(String inviteCode);

    List<User> findAllByRole(UserRole role);
}
