package com.project.ShopApp.configuration;

import com.project.ShopApp.filter.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(request -> {
                    request
                            .requestMatchers(
                                    String.format("%s/users/register", apiPrefix),
                                    String.format("%s/users/login", apiPrefix)
                            )
                            .permitAll()
                            .requestMatchers(
                                    "/api-docs",
                                    "/api-docs/**",
                                    "/swagger-resource",
                                    "/swagger-resource/**",
                                    "/configuration/ui",
                                    "/configuration/security",
                                    "/swagger-ui/**",
                                    "/swagger-ui.html",
                                    "/swagger-ui/index.html",
                                    "/webjars/**"
                            )
                            .permitAll()
                            .requestMatchers(
                                    HttpMethod.GET, String.format("%s/categories/**", apiPrefix)
                            )
                            .permitAll()
                            .requestMatchers(
                                    HttpMethod.GET, String.format("%s/product/**", apiPrefix)
                            )
                            .permitAll()
                            .requestMatchers(
                                    HttpMethod.GET, String.format("%s/orders/**", apiPrefix)
                            )
                            .permitAll()
                            .requestMatchers(
                                    HttpMethod.GET, String.format("%s/shopping-cart/**", apiPrefix)
                            )
                            .permitAll()
                            .anyRequest().authenticated();
                })
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
