package org.example.apigateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);
    private final String secretKey = "ThanosAndDoctorDoomAreBestVillainsInMarvelComics";

    public AuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getPath().toString();

            if (path.startsWith("/auth") || path.startsWith("/public")) {
                return chain.filter(exchange);
            }

            HttpHeaders headers = exchange.getRequest().getHeaders();
            if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
                log.warn("Request without Authorization header");
                return unauthorizedResponse("Authorization header is missing");
            }

            String token = headers.getFirst(HttpHeaders.AUTHORIZATION);
            if (token == null || !token.startsWith("Bearer ")) {
                log.warn("Invalid token");
                return unauthorizedResponse("Invalid token");
            }

            token = token.substring(7);

            try {
                Claims claims = validateToken(token);
                String userId = claims.getSubject();
                String role = claims.get("role", String.class);

                ServerHttpRequest request = exchange.getRequest().mutate()
                        .header("X-User-Id", userId)
                        .header("X-User-Role", role != null ? role : "USER")
                        .build();

                return chain.filter(exchange.mutate().request(request).build());
            } catch (Exception e) {
                log.error("Error during token validation", e);
                return unauthorizedResponse("Token is invalid or expired");
            }
        };
    }

    private Claims validateToken(String token) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Mono<Void> unauthorizedResponse(String message) {
        return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, message));
    }

    public static class Config {
    }
}
