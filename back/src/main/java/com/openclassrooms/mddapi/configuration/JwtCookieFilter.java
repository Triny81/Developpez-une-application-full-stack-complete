package com.openclassrooms.mddapi.configuration;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.openclassrooms.mddapi.service.JWTService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtCookieFilter extends OncePerRequestFilter {

    private final JWTService jwtService;

    public JwtCookieFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Arrays.stream(cookies)
                    .filter(cookie -> "jwt".equals(cookie.getName()))
                    .findFirst()
                    .ifPresent(cookie -> {
                        try {
                            String token = cookie.getValue();

                            List<String> roles = jwtService.extractRoles(token);

                            List<SimpleGrantedAuthority> authorities = roles.stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .collect(Collectors.toList());

                            Jwt jwt = jwtService.decodeToken(token);

                            Authentication auth = new JwtAuthenticationToken(jwt, authorities);
                            SecurityContextHolder.getContext().setAuthentication(auth);
                        } catch (Exception e) {
                        }
                    });
        }

        filterChain.doFilter(request, response);
    }
}

