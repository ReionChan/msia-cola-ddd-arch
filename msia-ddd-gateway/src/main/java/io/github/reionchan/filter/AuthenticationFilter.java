package io.github.reionchan.filter;

import com.alibaba.cola.dto.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.reionchan.properties.AuthProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

import static io.github.reionchan.commons.validation.CommonUtil.isEmpty;
import static io.github.reionchan.commons.validation.CommonUtil.isNotEmpty;
import static io.github.reionchan.consts.CommonConst.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 网关请求认证拦截
 *
 * @author Reion
 * @date 2023-12-24
 **/
@Slf4j
@Component
@EnableConfigurationProperties(AuthProperties.class)
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private ObjectMapper objectMapper;
    private AuthProperties authProperties;
    private AntPathMatcher skipAuthMatcher = new AntPathMatcher();


    @Autowired
    public void setAuthProperties(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }
    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest req = exchange.getRequest();
        String path = req.getURI().getPath();
        // 判断是否为放开的请求路径前缀
        if (isNotEmpty(authProperties.getAllSkipPath()) &&
                authProperties.getAllSkipPath().parallelStream().anyMatch(pattern -> skipAuthMatcher.match(pattern, path))) {
            log.debug("跳过路径：{}", path);
            exchange.mutate().request(req.mutate()
                    .headers(h -> h.remove(AUTH_TOKEN_HEADER))
                    .header(HEADER_SKIP_AUTH, Boolean.TRUE.toString()).build()).build();
            return chain.filter(exchange);
        }
        String authStr = req.getHeaders().getFirst(AUTH_TOKEN_HEADER);
        if (isEmpty(authStr)) {
            return unAuth(exchange.getResponse(), "缺失授权令牌");
        }
        if (!authStr.startsWith(AUTH_TOKEN_PREFIX)) {
            return unAuth(exchange.getResponse(), "授权令牌格式错误，请以'Bearer '开头");
        }

        return chain.filter(exchange);
    }

    private Mono<Void> unAuth(ServerHttpResponse resp, String msg) {
        resp.setStatusCode(HttpStatus.UNAUTHORIZED);
        resp.getHeaders().add("Content-Type", APPLICATION_JSON_VALUE);
        String result = "";
        try {
            result = objectMapper.writeValueAsString(Response.buildFailure(String.valueOf(HttpStatus.UNAUTHORIZED), msg));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        DataBuffer buffer = resp.bufferFactory().wrap(result.getBytes(StandardCharsets.UTF_8));
        return resp.writeWith(Flux.just(buffer));
    }
}
