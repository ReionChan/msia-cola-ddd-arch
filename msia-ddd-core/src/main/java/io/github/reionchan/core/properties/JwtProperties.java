package io.github.reionchan.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * JWT 配置属性类
 *
 * @author Reion
 * @date 2023-04-28
 **/
@ConfigurationProperties(prefix = "msia.jwt")
public class JwtProperties {
    private String issuer = "MSIA";
    private String subject = "Auth";
    private String secret = "DefaultSecretKey";
    private Integer validMinutes = 10;
    private Integer refreshValidMinutes = 3 * 24 * 60;

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Integer getValidMinutes() {
        return validMinutes;
    }

    public void setValidMinutes(Integer validMinutes) {
        this.validMinutes = validMinutes;
    }

    public Integer getRefreshValidMinutes() {
        return refreshValidMinutes;
    }

    public void setRefreshValidMinutes(Integer refreshValidMinutes) {
        this.refreshValidMinutes = refreshValidMinutes;
    }
}
