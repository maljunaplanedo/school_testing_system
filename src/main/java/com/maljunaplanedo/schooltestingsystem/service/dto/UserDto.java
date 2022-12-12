package com.maljunaplanedo.schooltestingsystem.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.maljunaplanedo.schooltestingsystem.model.User;
import com.maljunaplanedo.schooltestingsystem.model.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private Long id;

    private String username;

    private UserRole role;

    private String inviteCode;

    private String firstName;

    private String lastName;

    private ClassDto schoolClass;

    private List<StudentTaskDto> studentTasks;

    private UserDto(User user, boolean includeStudentTasks) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.inviteCode = user.getInviteCode();

        if (user.getRole() == UserRole.STUDENT) {
            var schoolClass = user.getSchoolClass();
            this.schoolClass = ClassDto.brief(schoolClass);
        }

        if (includeStudentTasks) {
            this.studentTasks = user
                .getStudentTasks()
                .stream()
                .map(StudentTaskDto::brief)
                .toList();
        }
    }

    public static UserDto full(User user) {
        return new UserDto(user, true);
    }

    public static UserDto brief(User user) {
        return new UserDto(user, false);
    }
}
