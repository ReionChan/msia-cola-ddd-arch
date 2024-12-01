package io.github.reionchan.core.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.MissingClaimException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.reionchan.core.dto.UserDto;
import io.github.reionchan.core.properties.JwtProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static io.github.reionchan.commons.validation.CommonUtil.*;

/**
 * JWT 工具类
 *
 * @author Reion
 * @date 2023-04-28
 **/
@Slf4j
@Data
@Component
@EnableConfigurationProperties(JwtProperties.class)
public class JwtUtil {

    public static final String CLAIM_USER_ID = "uid";
    public static final String CLAIM_USER_NAME = "una";
    public static final String CLAIM_USER_ROLES = "rol";
    public static final String CLAIM_USER_STATUS = "ust";
    public static final String CLAIM_TOKEN_TYPE = "tty";

    private static ApplicationContext context;

    private Algorithm algorithm;
    private Integer validMinutes;
    private Integer refreshValidMinutes;
    private JWTCreator.Builder builder;
    private JWTVerifier verifier;

    public JwtUtil(JwtProperties config) {
        Assert.isTrue(isNotEmpty(config.getSecret()), "secret must not empty");
        Assert.isTrue(isNotEmpty(config.getIssuer()), "issuer must not empty");
        Assert.isTrue(isNotEmpty(config.getSubject()), "subject must not empty");
        Assert.isTrue(isNotEmpty(config.getValidMinutes()) && config.getValidMinutes() > 0, "valid-minutes must be a positive number");
        Assert.isTrue(isNotEmpty(config.getRefreshValidMinutes()) && config.getRefreshValidMinutes() > 0, "refresh-valid-minutes must be a positive number");
        this.algorithm = Algorithm.HMAC512(config.getSecret());
        this.validMinutes = config.getValidMinutes();
        this.refreshValidMinutes = config.getRefreshValidMinutes();
        this.builder = JWT.create().withIssuer(config.getIssuer()).withSubject(config.getSubject());
        this.verifier = JWT.require(algorithm).withIssuer(config.getIssuer()).withSubject(config.getSubject())
                .withClaimPresence(CLAIM_TOKEN_TYPE).withClaimPresence(CLAIM_USER_ID)
                .withClaimPresence(CLAIM_USER_NAME).withClaimPresence(CLAIM_USER_ROLES)
                .withClaimPresence(CLAIM_USER_STATUS).build();
    }

    /**
     * 签发用户认证 JWT
     *
     * @param userDto 用户详情
     * @return JWT 令牌
     */
    public String createToken(UserDto userDto, boolean isRefresh, Integer innerValidMinutes) {
        Assert.notNull(userDto, "details must not null");
        Long userId = userDto.getId();
        Assert.isTrue(isNotEmpty(userId), "userid must not empty");
        String userName = userDto.getUserName();
        Assert.isTrue(Strings.isNotBlank(userName), "username must not empty");
        String roles =  userDto.getRoles();
        Assert.isTrue(isNotEmpty(roles), "roles must not empty");
        Byte status = userDto.getStatus();
        Assert.notNull(status, "status must not null");

        int valMinutes = isRefresh? refreshValidMinutes : validMinutes;
        int tokenType = isRefresh? 1 : 0;

        return builder.withClaim(CLAIM_TOKEN_TYPE, tokenType).withClaim(CLAIM_USER_ID, userId)
                .withClaim(CLAIM_USER_NAME, userName).withClaim(CLAIM_USER_ROLES, roles)
                .withClaim(CLAIM_USER_STATUS, status.intValue())
                .withExpiresAt(Instant.now().plus(valMinutes, ChronoUnit.MINUTES)).sign(algorithm);
    }

    public String createToken(UserDto userDto, boolean isRefresh) {
        return createToken(userDto, isRefresh, null);
    }

    /**
     * 校验 JWT
     *
     * @param token JWT 令牌
     * @return 用户详情
     */
    public UserDto verifierToken(String token, boolean isRefresh) {
        Assert.isTrue(Strings.isNotBlank(token), "JWT token string must not null");
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            Integer tokenType = decodedJWT.getClaim(CLAIM_TOKEN_TYPE).asInt();
            if (isRefresh) {
                Assert.isTrue(tokenType == 1, "JWT token type must be 1");
            } else {
                Assert.isTrue(tokenType == 0, "JWT token type must be 0");
            }
            Long userId = decodedJWT.getClaim(CLAIM_USER_ID).asLong();
            String userName = decodedJWT.getClaim(CLAIM_USER_NAME).asString();
            String roles = decodedJWT.getClaim(CLAIM_USER_ROLES).asString();
            Integer status = decodedJWT.getClaim(CLAIM_USER_STATUS).asInt();
            UserDto userDto = new UserDto();
            userDto.setId(userId);
            userDto.setUserName(userName);
            userDto.setRoles(roles);
            userDto.setStatus(status.byteValue());
            return userDto;
        } catch (TokenExpiredException expiredException) {
            log.error("JWT 已过期");
        } catch (MissingClaimException e) {
            log.error("JWT 未包含有效信息");
        } catch (IllegalArgumentException e) {
            log.error("JWT 类型错误", e);
        } catch (Exception e) {
            log.error("JWT 无效", e);
        }
        return null;
    }
    public boolean isAccessTokenExpired(String accessToken) {
        Assert.isTrue(Strings.isNotBlank(accessToken), "JWT token string must not null");
        try {
            DecodedJWT decodedJWT = verifier.verify(accessToken);
            Integer tokenType = decodedJWT.getClaim(CLAIM_TOKEN_TYPE).asInt();
            Assert.isTrue(tokenType == 0, "非访问令牌");
            return false;
        } catch (TokenExpiredException expiredException) {
            return true;
        } catch (Exception e) {
            throw new RuntimeException("错误的访问令牌");
        }
    }
}
