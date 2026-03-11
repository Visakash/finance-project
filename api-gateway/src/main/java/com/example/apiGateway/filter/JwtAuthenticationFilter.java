package com.example.apiGateway.filter;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.*;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Order(1)
@Slf4j
public class JwtAuthenticationFilter implements GlobalFilter {

    @Value("${jwt.secret}")
    private String secret;

    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/customer/login",
            "/api/customer/register",
            "/api/customer/forgot-password",
            "/api/customer/verify-otp-reset"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (PUBLIC_PATHS.stream().anyMatch(path::startsWith))
            return chain.filter(exchange);

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Missing Authorization header: {}", path);
            return reject(exchange);
        }

        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret.getBytes()).build()
                    .parseClaimsJws(authHeader.substring(7));
        } catch (ExpiredJwtException e) {
            log.warn("Expired token: {}", path); return reject(exchange);
        } catch (SignatureException | MalformedJwtException e) {
            log.warn("Invalid token: {}", path); return reject(exchange);
        }

        return chain.filter(exchange);
    }

    private Mono<Void> reject(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}