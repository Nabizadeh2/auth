package com.atl23.apigateway.config;

import com.atl23.apigateway.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
@RequiredArgsConstructor
public class RouteConfig {

    private final JwtFilter jwtFilter;

    @Value("${services.auth}")
    private String auth;


    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()

                .route("auth", r -> r
                        .path("/api/v1/auth/**")
                        .uri(auth))
                .build();
    }
}
