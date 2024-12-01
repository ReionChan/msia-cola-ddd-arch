package io.github.reionchan.core.security;

import io.github.reionchan.core.consts.UserStatus;
import io.github.reionchan.core.dto.UserDto;
import io.github.reionchan.core.properties.AuthProperties;
import io.github.reionchan.core.dto.UserDetailDto;
import io.github.reionchan.core.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static io.github.reionchan.commons.validation.CommonUtil.*;
import static io.github.reionchan.core.consts.CommonConst.AUTH_TOKEN_HEADER;

/**
 * 认证工具类
 *
 * @author Reion
 * @date 2023-12-25
 **/
@Slf4j
@Component
@EnableConfigurationProperties(AuthProperties.class)
public class AuthUtil {

    private static final String AUTH_USER_ATTRIBUTE = AuthUtil.class.getName() + ".UserAttribute";

    private static JwtUtil jwtUtil;
    private static AuthProperties properties;
    private static AntPathMatcher skipAuthMatcher = new AntPathMatcher();

    public AuthUtil(JwtUtil jwtUtil, AuthProperties properties) {
        init(jwtUtil, properties);
    }
    private static void init(JwtUtil jwt, AuthProperties authProperties) {
        jwtUtil = jwt;
        properties = authProperties;
    }

    public static String getCurrentRequestPath() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (isEmpty(requestAttributes)) {
            return null;
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = attributes.getRequest();
        return request.getServletPath();
    }
    public static boolean isSkipAuthDest(String destPath) {
        if (isEmpty(destPath) || isEmpty(properties.getAllSkipPath())) {
            return false;
        }
        return properties.getAllSkipPath().parallelStream().anyMatch(pattern -> skipAuthMatcher.match(pattern, destPath));
    }

    public static boolean isSystemRest(String method, String destPath) {
        if (isEmpty(properties.getSystemRestPath())) return false;
        return properties.getSystemRestPath().parallelStream().anyMatch(m ->
                method.equalsIgnoreCase(m.getMethod()) && destPath.matches(m.getPattern()));
    }

    public static String getHeaderToken() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (isEmpty(requestAttributes)) {
            return null;
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader(AUTH_TOKEN_HEADER);
        Assert.isTrue(isNotEmpty(token), "访问令牌缺失");
        return token;
    }

    @Cacheable(cacheNames = "adminToken", key = "#root.methodName")
    public String getAdminToken() {
        UserDto adminDto = new UserDto();
        adminDto.setId(properties.getVirtualAdmin().getId());
        adminDto.setUserName(properties.getVirtualAdmin().getUserName());
        adminDto.setRoles(properties.getVirtualAdmin().getRoles());
        adminDto.setStatus(UserStatus.ENABLED.getValue());
        return jwtUtil.createToken(adminDto, false, 1140);
    }

    public static void checkIsLegalUser(Long userId) {
        UsernamePasswordAuthenticationToken authentication =
            (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if (isEmpty(authentication)) {
            throw new UserNotLoginException("未登录");
        }
        UserDetailDto userDto = (UserDetailDto) authentication.getPrincipal();
        if (userDto.getId().equals(userId)) return;
        throw new UserNotPermitException("无权限");
    }
}
