package com.atl23.apigateway.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;

@Slf4j
@Component
public class JwtFilter implements GatewayFilter {

    @Value("${app.jwt.secret}")
    private String SECRET_KEY;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            var authFromHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

            String token = extractJwtFromRequest(authFromHeader);

            if (token == null || !validateToken(token)) {
                log.error("Invalid or missing JWT token");
                return handleUnauthorized(exchange);
            }
            ServerHttpRequest request = exchange.getRequest()
                    .mutate()
                    .header("appVersion", "1.0.0")

                    .header("Username", getUsername(token))
                    .build();

            ServerWebExchange mutatedExchange = exchange.mutate().request(request).build();

            return chain.filter(mutatedExchange);
//      exchange.getRequest().mutate().header("Username", getUsername(token));
//      return chain.filter(exchange);

        } catch (Exception e) {
            log.error("Error processing JWT token", e);
            return handleUnauthorized(exchange);
        }

    }

    private Mono<Void> handleUnauthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        return response.setComplete();
    }


    private String extractJwtFromRequest(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
    private boolean validateToken(String token) {
        try {
            var parser =  Jwts
                    .parser()
                    .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build();

            var exp = parser.parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();

            return exp.after(new Date());

        } catch (Exception e) {
            return false;
        }
    }


    private String getUsername(String token) {
        try {
            var parser =  Jwts
                    .parser()
                    .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build();

            return parser.parseSignedClaims(token)
                    .getPayload()
                    .getSubject();

        } catch (Exception e) {
            return null;
        }
    }
}
