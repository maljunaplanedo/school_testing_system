package com.maljunaplanedo.schooltestingsystem.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maljunaplanedo.schooltestingsystem.dto.LoginFormDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        var objectMapper = new ObjectMapper();
        LoginFormDto credentials;
        try {
            credentials = objectMapper.readValue(request.getReader(), LoginFormDto.class);
        } catch(IOException ioException) {
            credentials = new LoginFormDto();
        }
        request.setAttribute("username", credentials.getUsername());
        request.setAttribute("password", credentials.getPassword());

        return super.attemptAuthentication(request, response);
    }

    @Override
    @Nullable
    public String obtainUsername(HttpServletRequest request) {
        return (String) request.getAttribute("username");
    }

    @Override
    @Nullable
    public String obtainPassword(HttpServletRequest request) {
        return (String) request.getAttribute("password");
    }
}
