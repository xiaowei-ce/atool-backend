package org.example.atool.configs;

import org.example.atool.components.JWTFilter;
import org.example.atool.components.LogoutHandler;
import org.example.atool.components.UnAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConf {

    private final UnAuthenticationEntryPoint unAuthenticationEntryPoint;
    private final LogoutHandler logoutHandler;

    public SecurityConf(UnAuthenticationEntryPoint unAuthenticationEntryPoint, LogoutHandler logoutHandler) {
        this.unAuthenticationEntryPoint = unAuthenticationEntryPoint;
        this.logoutHandler = logoutHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security, JWTFilter jWTFilter) throws Exception {
        security.csrf(new Customizer<CsrfConfigurer<HttpSecurity>>() {
                    @Override
                    public void customize(CsrfConfigurer<HttpSecurity> configurer) {
                        configurer.disable();
                    }
                })
                .sessionManagement(new Customizer<SessionManagementConfigurer<HttpSecurity>>() {
                    @Override
                    public void customize(SessionManagementConfigurer<HttpSecurity> configurer) {
                        configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    }
                })
                .authorizeHttpRequests(new Customizer<AuthorizeHttpRequestsConfigurer<org.springframework.security.config.annotation.web.builders.HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>() {
                    @Override
                    public void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
                        registry.requestMatchers(
                                "/user/login",
                                "/user/register",
                                "/captcha/send"
                        ).permitAll();
                        registry.anyRequest().authenticated();
                    }
                })

                .addFilterBefore(jWTFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(new Customizer<ExceptionHandlingConfigurer<HttpSecurity>>() {
                    @Override
                    public void customize(ExceptionHandlingConfigurer<HttpSecurity> configurer) {
                        configurer.authenticationEntryPoint(unAuthenticationEntryPoint);

                    }
                })

                .logout(new Customizer<LogoutConfigurer<HttpSecurity>>() {
                    @Override
                    public void customize(LogoutConfigurer<HttpSecurity> configurer) {
                        configurer.logoutUrl("/user/logout")
                                .logoutSuccessHandler(logoutHandler);
                    }
                });

        return security.build();


    }
}
