package com.bar.bms.config;

import com.bar.bms.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register", "/admin/register-boss", "/forgot-password", "/images/**", "/css/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("BOSS")
                        .requestMatchers("/worker/**").hasRole("WORKER")
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(successHandler())
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            boolean isBoss = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_BOSS"));
            boolean isWorker = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_WORKER"));

            if (isBoss) {
                response.sendRedirect("/admin/dashboard");
            }  else if (isWorker) {
                response.sendRedirect("/worker/dashboard");
            } else {
                response.sendRedirect("/login");
            }
        };
    }
}