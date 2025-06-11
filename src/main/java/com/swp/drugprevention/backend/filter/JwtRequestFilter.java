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

    private static final List<String> PUBLIC_URLS = List.of("/login","/register","/send-reset-otp","reset-password","/logout");

//nơi logic chính của bộ lọc được thực thi
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();

        if (PUBLIC_URLS.contains(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = null;
        String email = null;

        //1. check the authorization header
        final String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader.substring(7);
        }

        //2. If not found in header, check cookies
        if (jwt == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie: cookies) {
                    if ("jwt".equals(cookie.getName())) {
                        jwt = cookie.getValue();
                        break;
                    }
                }
            }
        }

        //3.validate the token and set security context

        if (jwt != null) {

            email = jwtUtil.extractEmail(jwt);
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = appUserDetailsService.loadUserByUsername(email);
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }

        filterChain.doFilter(request, response); //lọc xong thì cho phép yêu cầu đến controller với quyền hạn đã xác thực

    }
}
