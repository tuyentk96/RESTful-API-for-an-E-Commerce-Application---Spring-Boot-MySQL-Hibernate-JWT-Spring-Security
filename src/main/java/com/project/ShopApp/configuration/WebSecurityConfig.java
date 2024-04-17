package com.project.ShopApp.configuration;

import com.project.ShopApp.filter.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig{
    private final JwtTokenFilter jwtTokenFilter;
    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

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
                                    HttpMethod.GET,String.format("%s/categories/**",apiPrefix)
                            )
                            .permitAll()
                            .requestMatchers(
                                    HttpMethod.POST,String.format("%s/categories/**",apiPrefix)
                            )
                            .hasRole("ADMIN")
                            .requestMatchers(
                                    HttpMethod.PUT,String.format("%s/categories/**",apiPrefix)
                            )
                            .hasRole("ADMIN")
                            .requestMatchers(
                                    HttpMethod.DELETE,String.format("%s/categories/**",apiPrefix)
                            )
                            .hasRole("ADMIN")
                            .requestMatchers(
                                    HttpMethod.GET,String.format("%s/product/**",apiPrefix)
                            )
                            .permitAll()
                            .requestMatchers(
                                    HttpMethod.POST,String.format("%s/product/**",apiPrefix)
                            )
                            .hasRole("ADMIN")
                            .requestMatchers(
                                    HttpMethod.PUT,String.format("%s/product/**",apiPrefix)
                            )
                            .hasRole("ADMIN")
                            .requestMatchers(
                                    HttpMethod.DELETE,String.format("%s/product/**",apiPrefix)
                            )
                            .hasRole("ADMIN")
                            .requestMatchers(
                                    HttpMethod.GET,String.format("%s/orders/**",apiPrefix)
                            )
                            .hasAnyRole("ADMIN","USER")
                            .requestMatchers(
                                    HttpMethod.POST,String.format("%s/orders/**",apiPrefix)
                            )
                            .hasRole("USER")
                            .requestMatchers(
                                    HttpMethod.PUT,String.format("%s/orders/**",apiPrefix)
                            )
                            .hasRole("ADMIN")
                            .requestMatchers(
                                    HttpMethod.DELETE,String.format("%s/orders/**",apiPrefix)
                            )
                            .hasRole("ADMIN")
                            .requestMatchers(
                                    HttpMethod.GET,String.format("%s/order-details/**",apiPrefix)
                            )
                            .hasAnyRole("ADMIN","USER")
                            .requestMatchers(
                                    HttpMethod.POST,String.format("%s/order-details/**",apiPrefix)
                            )
                            .hasRole("USER")
                            .requestMatchers(
                                    HttpMethod.PUT,String.format("%s/order-details/**",apiPrefix)
                            )
                            .hasRole("ADMIN")
                            .requestMatchers(
                                    HttpMethod.DELETE,String.format("%s/order-details/**",apiPrefix)
                            )
                            .hasRole("ADMIN")
                            .requestMatchers(
                                    HttpMethod.PUT,String.format("%s/shopping-cart/**",apiPrefix)
                            )
                            .hasAnyRole("ADMIN","USER")
                            .requestMatchers(
                                    HttpMethod.DELETE,String.format("%s/shopping-cart/**",apiPrefix)
                            )
                            .hasAnyRole("ADMIN","USER")
                            .requestMatchers(
                                    HttpMethod.GET,String.format("%s/shopping-cart/**",apiPrefix)
                            )
                            .hasAnyRole("ADMIN","USER")
                            .requestMatchers(
                                    HttpMethod.POST,String.format("%s/import-data/**",apiPrefix)
                            ).hasRole("ADMIN")
                            .requestMatchers(
                                    HttpMethod.POST,String.format("%s/export-data/**",apiPrefix)
                            ).hasRole("ADMIN")
                            .anyRequest().authenticated();
                })
                .csrf(AbstractHttpConfigurer::disable);
//        http.securityMatcher(String.valueOf(EndpointRequest.toAnyEndpoint()));
        return http.build();
    }
}
