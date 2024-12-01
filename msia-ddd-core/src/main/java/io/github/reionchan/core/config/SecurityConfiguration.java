package io.github.reionchan.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.reionchan.core.exception.GlobalExceptionHandler;
import io.github.reionchan.core.filter.JwtAuthenticationTokenFilter;
import io.github.reionchan.core.properties.AuthProperties;
import io.github.reionchan.core.security.JwtUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

import static io.github.reionchan.commons.validation.CommonUtil.isNotEmpty;

/**
 * 安全控制配置
 *
 * @author Reion
 * @date 2024-01-09
 **/
@Slf4j
@Configuration
@EnableConfigurationProperties(AuthProperties.class)
@ConditionalOnMissingClass({"org.springframework.cloud.gateway.config.GatewayAutoConfiguration"})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityConfiguration {

    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private AuthProperties authProperties;
    @Value("${spring.application.name}")
    private String module;

    @Bean
    public SecurityFilterChain customizeSecurityFilterChain(HttpSecurity http) throws Exception {
        log.info("--- 对容器中自动装配的默认 HttpSecurity 进行定制化配置 ---");
        List<AntPathRequestMatcher> skipMatchers = authProperties.getSkipPathWithCommons(module)
                .stream().map(AntPathRequestMatcher::new).toList();
        http
            .formLogin(login -> login.disable())
            .logout(logout -> logout.disable())
            .csrf(csrf -> csrf.disable())
            .httpBasic(basic -> basic.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 在 UsernamePasswordAuthenticationFilter 添加 JWT 认证过滤器
            .addFilterBefore(new JwtAuthenticationTokenFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
            // 处理认证授权异常
            .exceptionHandling(exh -> exh
                .accessDeniedHandler(GlobalExceptionHandler::handleAuthException)
                .authenticationEntryPoint(GlobalExceptionHandler::handleAuthException))
            // 认证
            .authorizeHttpRequests(auth -> {
                if (isNotEmpty(skipMatchers)) {
                    auth.requestMatchers(skipMatchers.toArray(AntPathRequestMatcher[]::new)).permitAll();
                }
                auth.anyRequest().authenticated();
            }

        );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("--- 注册一个密码加密器 NoOpPasswordEncoder ---");
        return NoOpPasswordEncoder.getInstance();
    }
}
