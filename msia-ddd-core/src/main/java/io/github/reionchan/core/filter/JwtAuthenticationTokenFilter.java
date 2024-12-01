package io.github.reionchan.core.filter;

import io.github.reionchan.core.dto.UserDetailDto;
import io.github.reionchan.core.dto.UserDto;
import io.github.reionchan.core.security.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 校验过滤器
 *
 * @author Reion
 * @date 2023-04-27
 **/
@Data
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

    private AuthenticationManager authenticationManager;
    private JwtAuthenticationConverter authenticationConverter;
    protected AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();


    public JwtAuthenticationTokenFilter(JwtUtil jwtUtil) {
        this.authenticationConverter = new JwtAuthenticationConverter(jwtUtil);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            UsernamePasswordAuthenticationToken authRequest = this.authenticationConverter.convert(request);
            if (authRequest == null) {
                log.debug("缺失令牌，跳过本过滤器");
                filterChain.doFilter(request, response);
                return;
            }
            String username = authRequest.getName();
            log.debug(String.format("用户 '%s' JWT 认证成功！", username));

            // 该步骤至关重要，由 JWT 解析形成的 Authentication 放入安全上下文，使得它后面的登录过滤器跳过登录拦截
            SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
            context.setAuthentication(authRequest);
            this.securityContextHolderStrategy.setContext(context);
        } catch (AuthenticationException ex) {
            this.securityContextHolderStrategy.clearContext();
            throw ex;
        }

        filterChain.doFilter(request, response);
    }

    @Data
    @Slf4j
    private static class JwtAuthenticationConverter implements AuthenticationConverter {

        public static final String AUTHENTICATION_SCHEME_BEARER = "Bearer";
        private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;
        private JwtUtil jwtUtil;

        public JwtAuthenticationConverter(JwtUtil jwtUtil) {
            this(new WebAuthenticationDetailsSource(), jwtUtil);
        }

        public JwtAuthenticationConverter(
                AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource, JwtUtil jwtUtil) {
            this.authenticationDetailsSource = authenticationDetailsSource;
            this.jwtUtil = jwtUtil;
        }

        @Override
        public UsernamePasswordAuthenticationToken convert(HttpServletRequest request) {
            try {
                String header = request.getHeader(HttpHeaders.AUTHORIZATION);
                // 令牌为空直接返回，后续将由 AnonymousAuthenticationFilter 创建匿名认证对象
                if (header == null) {
                    return null;
                }

                header = header.trim();
                Assert.isTrue(StringUtils.startsWithIgnoreCase(header, AUTHENTICATION_SCHEME_BEARER)
                    && header.length()>7
                    && !header.equalsIgnoreCase(AUTHENTICATION_SCHEME_BEARER), "无效令牌格式");
                String jwtToken = header.substring(7);

                log.debug("接收到令牌：" + jwtToken);

                UserDto userDto = jwtUtil.verifierToken(jwtToken, false);
                Assert.notNull(userDto, "无效令牌");
                UserDetails userDetails = UserDetailDto.builder(userDto).build();

                UsernamePasswordAuthenticationToken result = UsernamePasswordAuthenticationToken
                        .authenticated(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                result.setDetails(authenticationDetailsSource.buildDetails(request));
                return result;
            }
            catch (Exception e) {
                throw new BadCredentialsException("令牌转换异常", e);
            }
        }
    }
}
