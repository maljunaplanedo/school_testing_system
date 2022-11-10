package com.maljunaplanedo.schooltestingsystem.staticforwarding;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Set;

@Configuration
public class StaticForwardingConfiguration implements WebMvcConfigurer {
    private static final String REDIRECT = "forward:/static/index.html";

    private static final Set<String> PATTERNS = Set.of(
        "/",
        "/{x:^(?!api|static$).*$}/**",
        "/{x:^(?!api|static$).*$}"
    );

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        PATTERNS.forEach(pattern ->
            registry.addViewController(pattern).setViewName(REDIRECT)
        );
    }
}
