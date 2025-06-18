package com.swp.drugprevention.backend.filter;

import com.swp.drugprevention.backend.service.AppUserDetailsService;
import com.swp.drugprevention.backend.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

//là một bộ lọc dùng để lọc các yêu cầu http từ client -> thành công hoặc thất bại dựa vào token
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter { //xử lý token đã được client gửi lên

    private final AppUserDetailsService appUserDetailsService;
    private final JwtUtil jwtUtil;

    private static final List<String> PUBLIC_URLS = List.of(
            "/login",
            "/register",
            "/send-reset-otp",
            "/reset-password",
            "/logout",
            "/verify-otp" // Thêm dòng này
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();
        System.out.println("Processing path: " + path);

        if (PUBLIC_URLS.contains(path)) {
            System.out.println("Public URL, skipping authentication");
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = null;
        String email = null;

        final String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Authorization header: " + authorizationHeader);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            System.out.println("JWT from header: " + jwt);
        }

        if (jwt == null) {
            Cookie[] cookies = request.getCookies();
            System.out.println("Cookies available: " + (cookies != null));
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("jwt".equals(cookie.getName())) {
                        jwt = cookie.getValue();
                        System.out.println("JWT from cookie: " + jwt);
                        break;
                    }
                }
            }
        }

        if (jwt != null) {
            email = jwtUtil.extractEmail(jwt);
            System.out.println("Extracted email: " + email);
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    UserDetails userDetails = appUserDetailsService.loadUserByUsername(email);
                    System.out.println("UserDetails loaded: " + (userDetails != null ? userDetails.getUsername() : "null"));
                    if (jwtUtil.validateToken(jwt, userDetails)) {
                        System.out.println("Token validated successfully");
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        System.out.println("Authentication set for: " + email);
                    } else {
                        System.out.println("Token validation failed");
                    }
                } catch (Exception e) {
                    System.out.println("Error loading user details: " + e.getMessage());
                }
            }
        } else {
            System.out.println("No JWT found");
        }

        filterChain.doFilter(request, response);
    }
}
