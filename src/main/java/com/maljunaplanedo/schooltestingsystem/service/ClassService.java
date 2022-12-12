package com.maljunaplanedo.schooltestingsystem.service;

import com.maljunaplanedo.schooltestingsystem.service.dto.ClassDto;
import com.maljunaplanedo.schooltestingsystem.exception.BadDataFormatException;
import com.maljunaplanedo.schooltestingsystem.exception.ClassNameAlreadyUsedException;
import com.maljunaplanedo.schooltestingsystem.model.SchoolClass;
import com.maljunaplanedo.schooltestingsystem.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
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

    public SchoolClass findById(long id) throws BadDataFormatException {
        return classRepository
            .findById(id)
            .orElseThrow(() -> new BadDataFormatException("Class does not exist"));
    }

    public ClassDto getClass(long id) throws BadDataFormatException {
        return ClassDto.full(findById(id));
    }

    public List<ClassDto> getAllClasses() {
        return classRepository
            .findAll()
            .stream()
            .map(ClassDto::brief)
            .toList();
    }

    protected void saveClass(SchoolClass schoolClass, ClassDto classInfo)
            throws BadDataFormatException, ClassNameAlreadyUsedException {
        var name = classInfo.getName();

        if (badClassName(name)) {
            throw new BadDataFormatException("Bad data format");
        }
        if (classRepository.existsByName(name)) {
            throw new ClassNameAlreadyUsedException(String.format("Class name already used: %s", name));
        }

        schoolClass.setName(name);
        classRepository.save(schoolClass);
    }

    public void addClass(ClassDto classInfo)
            throws BadDataFormatException, ClassNameAlreadyUsedException {
        saveClass(new SchoolClass(), classInfo);
    }

    public void updateClass(long id, ClassDto classInfo)
            throws BadDataFormatException, ClassNameAlreadyUsedException {
        saveClass(findById(id), classInfo);
    }

    public void removeClass(long id) throws BadDataFormatException {
        classRepository.delete(findById(id));
    }
}
