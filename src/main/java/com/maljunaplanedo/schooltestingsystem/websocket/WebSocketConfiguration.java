package com.maljunaplanedo.schooltestingsystem.websocket;

import com.maljunaplanedo.schooltestingsystem.repository.UserRepository;
import com.maljunaplanedo.schooltestingsystem.service.StudentTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {
    private UserRepository userRepository;

    private StudentTaskService studentTaskService;

    @Autowired
    public void setUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setStudentTaskService(StudentTaskService studentTaskService) {
        this.studentTaskService = studentTaskService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new TaskRunnerHandler(userRepository, studentTaskService), "/api/live");
    }
}
