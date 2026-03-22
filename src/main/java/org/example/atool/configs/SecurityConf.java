package org.example.atool.configs;

import lombok.RequiredArgsConstructor;
import org.example.atool.components.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConf {

    private final UnAuthenticationEntryPoint unAuthenticationEntryPoint;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
    private final UnAccessDeniedHandler unAccessDeniedHandler;
    private final CorsConfigurationSource corsConfigurationSource;
    private final MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173/","http://sky.with.us.in"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setMaxAge(3600L);
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security, JWTFilter jWTFilter) throws Exception {
        security
                .authorizeHttpRequests(new Customizer<AuthorizeHttpRequestsConfigurer<org.springframework.security.config.annotation.web.builders.HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>() {
                    @Override
                    public void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
                        registry.requestMatchers(
                                        "/user/login",
                                        "/user/register",
                                        "/captcha/send",
                                        "/test"
                                ).permitAll();
                        registry.anyRequest().authenticated();
                    }
                })
                .csrf(new Customizer<CsrfConfigurer<HttpSecurity>>() {
                    @Override
                    public void customize(CsrfConfigurer<HttpSecurity> configurer) {
//                        configurer.ignoringRequestMatchers("/**");
                        configurer.disable();
                    }
                })
                .cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
                    @Override
                    public void customize(CorsConfigurer<HttpSecurity> configurer) {
                        configurer.configurationSource(corsConfigurationSource);
                    }
                })
                .sessionManagement(new Customizer<SessionManagementConfigurer<HttpSecurity>>() {
                    @Override
                    public void customize(SessionManagementConfigurer<HttpSecurity> configurer) {
                        configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    }
                })

                .addFilterBefore(jWTFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(new Customizer<ExceptionHandlingConfigurer<HttpSecurity>>() {
                    @Override
                    public void customize(ExceptionHandlingConfigurer<HttpSecurity> configurer) {
                        configurer.authenticationEntryPoint(unAuthenticationEntryPoint);
                        configurer.accessDeniedHandler(unAccessDeniedHandler);
                    }
                })


                .formLogin(new Customizer<FormLoginConfigurer<HttpSecurity>>() {
                    @Override
                    public void customize(FormLoginConfigurer<HttpSecurity> configurer) {
                        configurer.failureHandler(myAuthenticationFailureHandler);
                    }
                })

                .logout(new Customizer<LogoutConfigurer<HttpSecurity>>() {
                    @Override
                    public void customize(LogoutConfigurer<HttpSecurity> configurer) {
                        configurer.logoutSuccessHandler(customLogoutSuccessHandler)
                                .logoutUrl("/user/logout");
                    }
                });

        return security.build();
    }
}
