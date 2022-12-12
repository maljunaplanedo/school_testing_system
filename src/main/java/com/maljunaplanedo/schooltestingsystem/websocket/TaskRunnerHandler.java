package com.maljunaplanedo.schooltestingsystem.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maljunaplanedo.schooltestingsystem.exception.BadDataFormatException;
import com.maljunaplanedo.schooltestingsystem.exception.NoTaskExecutingException;
import com.maljunaplanedo.schooltestingsystem.model.User;
import com.maljunaplanedo.schooltestingsystem.model.UserRole;
import com.maljunaplanedo.schooltestingsystem.repository.UserRepository;
import com.maljunaplanedo.schooltestingsystem.service.StudentTaskService;
import lombok.AllArgsConstructor;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
public class TaskRunnerHandler extends TextWebSocketHandler {
    private final UserRepository userRepository;

    private final StudentTaskService studentTaskService;

    private final ConcurrentHashMap<Long, WebSocketSession> activeSessions = new ConcurrentHashMap<>();

    private Optional<User> currentUser(WebSocketSession session) {
        var principal = session.getPrincipal();
        if (principal == null) {
            return Optional.empty();
        }
        return userRepository.findByUsername(principal.getName());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        var user = currentUser(session);

        if (user.isEmpty() || user.get().getRole() != UserRole.STUDENT) {
            session.close(CloseStatus.BAD_DATA);
            return;
        }
        var userId = user.get().getId();

        var oldSession = activeSessions.put(userId, session);
        if (oldSession != null) {
            oldSession.close(CloseStatus.POLICY_VIOLATION);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        currentUser(session).ifPresent(user ->
            activeSessions.remove(user.getId(), session)
        );
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        var userOptional = currentUser(session);
        if (userOptional.isEmpty()) {
            return;
        }
        var user = userOptional.get();

        try {
            if (session.getId().equals(activeSessions.get(user.getId()).getId())) {
                var result
                    = studentTaskService.handleWebsocketCheckRequest(message.getPayload(), user);
                if (result.isEmpty()) {
                    session.close(CloseStatus.NORMAL);
                } else {
                    var mapper = new ObjectMapper();
                    session.sendMessage(new TextMessage(mapper.writeValueAsString(result.get())));
                }
            }
        } catch (BadDataFormatException exception) {
            session.close(CloseStatus.BAD_DATA);
        }
    }
}
