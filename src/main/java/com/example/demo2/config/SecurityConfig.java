package com.example.demo2.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.demo2.security.JwtAuthenticationFilter;
import com.example.demo2.security.handler.JwtAccessDeniedHandler;
import com.example.demo2.security.handler.JwtAuthenticationEntryPoint;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

        private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
        private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        public SecurityConfig(
                        JwtAccessDeniedHandler jwtAccessDeniedHandler,
                        JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                        JwtAuthenticationFilter jwtAuthenticationFilter) {
                this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
                this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                http

                                .csrf(csrf -> csrf.disable())

                                .cors(cors -> {
                                }) // ⭐ 開啟 CORS

                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                .exceptionHandling(ex -> ex
                                                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                                .accessDeniedHandler(jwtAccessDeniedHandler))

                                .authorizeHttpRequests(auth -> auth

                                                .requestMatchers(
                                                                "/auth/**",
                                                                "/admin/login",
                                                                "/announ/**",
                                                                "/statistics/**",
                                                                "/bills/**",
                                                                "/payment/callback",
                                                                "/calendar/**")
                                                .permitAll()
                                                // swagger顯示API
                                                .requestMatchers("/swagger-ui/**", "/v3/**").permitAll()

                                                // TODO: 【Phase 4】新增超級管理員密碼驗證和資料更新端點權限
                                                // 需要添加以下路由，所有需要 SUPER_ADMIN 角色：
                                                // - /modify/superadmin/verify-password (POST) - 密碼驗證
                                                // - /modify/superadmin/send-change-verify-code (POST) - 發送驗證碼
                                                // - /modify/superadmin/self (PUT) - 更新資料
                                                //
                                                // 修改方式：
                                                // .requestMatchers("/modify/superadmin",
                                                //                 "/modify/superadmin/verify-password",
                                                //                 "/modify/superadmin/send-change-verify-code",
                                                //                 "/modify/superadmin/self").hasRole("SUPER_ADMIN")
                                                .requestMatchers("/modify/superadmin").hasRole("SUPER_ADMIN")
                                                .requestMatchers("/modify/admin","/admin/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                                                .requestMatchers("/facility/**").hasAnyRole("ADMIN", "SUPER_ADMIN")

                                                .anyRequest().authenticated())
                                .addFilterBefore(
                                                jwtAuthenticationFilter,
                                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        // ⭐ CORS 設定
        @Bean
        public CorsConfigurationSource corsConfigurationSource() {

                CorsConfiguration config = new CorsConfiguration();

                config.setAllowedOrigins(List.of("http://localhost:4200"));
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(List.of("*"));
                config.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

                source.registerCorsConfiguration("/**", config);

                return source;
        }

}
