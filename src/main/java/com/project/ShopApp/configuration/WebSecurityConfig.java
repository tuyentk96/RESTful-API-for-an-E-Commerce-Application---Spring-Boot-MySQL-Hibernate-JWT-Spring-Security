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
                                    HttpMethod.GET,String.format("%s/categories/**",apiPrefix)
                            )
                            .hasAnyRole("ADMIN","USER")
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
                            .hasAnyRole("ADMIN","USER")
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
                            .anyRequest().authenticated();
                })
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
