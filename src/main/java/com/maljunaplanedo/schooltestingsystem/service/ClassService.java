package com.maljunaplanedo.schooltestingsystem.service;

import com.maljunaplanedo.schooltestingsystem.dto.ClassDto;
import com.maljunaplanedo.schooltestingsystem.dto.SmallUserInfoDto;
import com.maljunaplanedo.schooltestingsystem.exception.BadDataFormatException;
import com.maljunaplanedo.schooltestingsystem.exception.ClassNameAlreadyUsedException;
import com.maljunaplanedo.schooltestingsystem.model.SchoolClass;
import com.maljunaplanedo.schooltestingsystem.model.User;
import com.maljunaplanedo.schooltestingsystem.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassService {
    private ClassRepository classRepository;

    @Autowired
    public void setClassRepository(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    private boolean badClassName(@Nullable String className) {
        return className == null || className.isEmpty() || className.length() > 16;
    }

    @Transactional
    public void addClass(ClassDto classInfo) throws BadDataFormatException, ClassNameAlreadyUsedException {
        var name = classInfo.getName();

        if (badClassName(name)) {
            throw new BadDataFormatException("Bad data format");
        }
        if (classRepository.existsByName(name)) {
            throw new ClassNameAlreadyUsedException(String.format("Class name already used: %s", name));
        }

        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName(name);

        classRepository.save(schoolClass);
    }

    @Transactional
    public void removeClass(long id) throws BadDataFormatException {
        var schoolClass = classRepository
            .findById(id)
            .orElseThrow(() -> new BadDataFormatException("Class does not exist"));
        classRepository.delete(schoolClass);
    }

    @Transactional
    public List<SmallUserInfoDto> getStudents(long id) throws BadDataFormatException {
        var schoolClass = classRepository
            .findById(id)
            .orElseThrow(() -> new BadDataFormatException("Class does not exist"));

        return schoolClass
            .getStudents()
            .stream()
            .map(user -> {
                var userInfo = new SmallUserInfoDto();
                userInfo.setFirstName(user.getFirstName());
                userInfo.setLastName(user.getLastName());
                userInfo.setUsername(user.getUsername());
                userInfo.setInviteCode(user.getInviteCode());
                return userInfo;
            })
            .collect(Collectors.toList());
    }
}
