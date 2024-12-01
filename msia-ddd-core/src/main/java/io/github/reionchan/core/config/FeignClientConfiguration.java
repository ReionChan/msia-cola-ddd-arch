package io.github.reionchan.core.config;

import feign.Logger;
import feign.RequestInterceptor;
import io.github.reionchan.core.security.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import static io.github.reionchan.core.consts.CommonConst.AUTH_TOKEN_HEADER;
import static io.github.reionchan.core.consts.CommonConst.AUTH_TOKEN_PREFIX;
import static io.github.reionchan.commons.validation.CommonUtil.*;

/**
 * OpenFeign 客户端定制化配置类
 *
 * @author Reion
 * @date 2023-09-10
 **/
@Slf4j
public class FeignClientConfiguration {

    /**
     * <pre>
     * 设置 feign 日志打印等级
     *
     * 请同时在 application.yaml 文件指定 feign 客户端的日志级别为 DEBUG
     *  logging.level.your.feign.client.ClientName: DEBUG
     *  </pre>
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        // FULL 基本将打印请求及响应的详细信息
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor authRequestInterceptor(AuthUtil authUtil) {
        return (requestTemplate) -> {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            boolean isSkip = AuthUtil.isSkipAuthDest(AuthUtil.getCurrentRequestPath());
            // Web 上下文，且非网关跳过时，头部必有 token 信息
            if (isNotEmpty(requestAttributes) && !isSkip) {
                String accToken = AuthUtil.getHeaderToken();
                requestTemplate.header(AUTH_TOKEN_HEADER, accToken);
                log.debug("OpenFeign JWT headers: {}", accToken);
                return;
            }
            // 符合系统调用配置上下文，使用配置的身份生成 token 信息
            log.info("OpenFeign request {} {}", requestTemplate.method(), requestTemplate.path());
            if (AuthUtil.isSystemRest(requestTemplate.method(), requestTemplate.path())) {
                log.info("系统调用，生成系统管理员令牌");
                String accToken = authUtil.getAdminToken();
                requestTemplate.header(AUTH_TOKEN_HEADER, AUTH_TOKEN_PREFIX + accToken);
                log.debug("OpenFeign Admin JWT headers: {}", accToken);
                return;
            }
            log.debug("OpenFeign add None headers.");
        };
    }
}
