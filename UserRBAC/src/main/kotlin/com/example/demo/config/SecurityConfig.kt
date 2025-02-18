package com.example.demo.config

import com.example.demo.security.JwtAuthenticationFilter
import com.example.demo.security.JwtAuthorizationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.slf4j.LoggerFactory

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val jwtAuthorizationFilter: JwtAuthorizationFilter
) {

    private val logger = LoggerFactory.getLogger(SecurityConfig::class.java)

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        logger.info("Configuring security filter chain")

        http.csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers(HttpMethod.POST, "/users/register", "/users/login").permitAll() // Permit registration
            .requestMatchers("/users/all").hasAuthority("Admin")  // Users with ROLE_USER
            .requestMatchers("/departments/**").hasAuthority("Admin")  // Admin routes for ROLE_ADMIN
            .anyRequest().authenticated()  // All other routes require authentication
            .and()
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)  // JWT authentication before Spring's default
            .addFilterAfter(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter::class.java) // JWT authorization after authentication

        logger.info("Security filter chain configured successfully")
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        logger.info("Password encoder: BCryptPasswordEncoder")
        return BCryptPasswordEncoder()
    }
}
