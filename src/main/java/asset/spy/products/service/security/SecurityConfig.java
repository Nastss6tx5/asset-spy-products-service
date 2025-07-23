package asset.spy.products.service.security;

import asset.spy.auth.lib.exception.AccessDeniedExceptionSupplier;
import asset.spy.auth.lib.exception.AuthenticationExceptionSupplier;
import asset.spy.auth.lib.filter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtFilter jwtFilter,
                                                   AccessDeniedExceptionSupplier accessDeniedExceptionSupplier,
                                                   AuthenticationExceptionSupplier authenticationExceptionSupplier) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/v1/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationExceptionSupplier)
                        .accessDeniedHandler(accessDeniedExceptionSupplier))
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
