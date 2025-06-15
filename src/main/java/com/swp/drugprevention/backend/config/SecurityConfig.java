package com.swp.drugprevention.backend.config;

import com.swp.drugprevention.backend.filter.JwtRequestFilter;
import com.swp.drugprevention.backend.service.AppUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AppUserDetailsService appUserDetailsService;
    private final JwtRequestFilter jwtRequestFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->auth
                        .requestMatchers("/login","/google", "/register", "/send-reset-otp", "/reset-password", "/logout", "/send-otp", "/verify-otp", "/loginSuccess")

                        .permitAll().anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .oauth2Login(oauth2 ->oauth2
                        .defaultSuccessUrl("/loginSuccess", true)
                        .failureUrl("/loginFailure"))
                .sessionManagement(session ->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//mỗi lần gửi request là phải có token ở header để xác thực, vì dùng STATELESS không cho lưu session
                .logout(AbstractHttpConfigurer::disable) //vì đã không cho lưu session nên không có session để xóa nên tính năng logout mặc định của spring security ko cần thiết
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)//dùng để chèn bộ lọc jwtRequestFilter trước bộ lọc UsernamePasswordAuthenticationFilter, //mục đích là để bộ lọc jwt kiểm tra token có hợp lệ hay không trước đã
                .exceptionHandling(ex -> ex.authenticationEntryPoint(customAuthenticationEntryPoint)); //xác thực không thành công nghĩa là chưa đăng nhập thì nó sẽ hiện ra ngoại lệ do mình cài đặt ở customAuthenticationEntryPoint
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }

    // lien ket fe
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:8080"));
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(appUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authenticationProvider);
    }

}
