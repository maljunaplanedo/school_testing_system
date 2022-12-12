package com.maljunaplanedo.schooltestingsystem.annotation;

import com.maljunaplanedo.schooltestingsystem.model.TaskType;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface TaskChecker {
    TaskType value();
}
