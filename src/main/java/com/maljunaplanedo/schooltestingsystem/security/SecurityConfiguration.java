package com.maljunaplanedo.schooltestingsystem.security;

import com.maljunaplanedo.schooltestingsystem.model.UserRole;
import com.maljunaplanedo.schooltestingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import static javax.servlet.http.HttpServletResponse.*;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
            .map(SchoolUserDetails::new)
            .orElseThrow(() -> new UsernameNotFoundException(String.format("Username '%s' not found", username)));
    }

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .requestCache().disable()
            .csrf()
                .disable()
                // TODO: enable
                // .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                //.and()
            .authorizeRequests()
                .antMatchers("/api/admin/**").hasRole(UserRole.ADMIN.toString())
                .antMatchers("/api/teacher/**").hasRole(UserRole.TEACHER.toString())
                .antMatchers("/api/student/**", "/api/live/connect/**").hasRole(UserRole.STUDENT.toString())
                .antMatchers("/api/auth/login", "/api/auth/register").anonymous()
                .antMatchers("/api/auth/logout").authenticated()
                .antMatchers(HttpMethod.GET).permitAll()
                .anyRequest().denyAll()
                .and()
            .exceptionHandling()
                .accessDeniedHandler((req, res, ex) -> res.setStatus(SC_FORBIDDEN))
                .authenticationEntryPoint((req, res, ex) -> res.setStatus(SC_UNAUTHORIZED))
                .and()
            .apply(new JsonLoginConfigurer())
                .loginProcessingUrl("/api/auth/login")
                .successHandler((req, res, auth) -> res.setStatus(SC_OK))
                .failureHandler((req, res, ex) -> res.setStatus(SC_FORBIDDEN))
                .and()
            .logout()
                .logoutUrl("/api/auth/logout")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .deleteCookies("JSESSIONID");

        return http.build();
    }
}
