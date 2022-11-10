package com.maljunaplanedo.schooltestingsystem.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class JsonLoginConfigurer
    extends AbstractAuthenticationFilterConfigurer<
        HttpSecurity, JsonLoginConfigurer, UsernamePasswordAuthenticationFilter
    > {

    public JsonLoginConfigurer() {
        super(new JsonAuthenticationFilter(), null);
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, "POST");
    }
}
