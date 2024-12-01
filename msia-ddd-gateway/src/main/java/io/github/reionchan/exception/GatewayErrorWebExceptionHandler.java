package io.github.reionchan.exception;

import com.alibaba.cola.dto.Response;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;


/**
 * 网关异常处理器
 *
 * @author Reion
 * @date 2023-12-15
 **/
public class GatewayErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {

    public GatewayErrorWebExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources,
                                           ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resources, errorProperties, applicationContext);
    }

    @Override
    public Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> error = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        Integer statusCode = (Integer) error.get("status");
        HttpStatus.Series series = HttpStatus.Series.resolve(statusCode);

        BodyInserter<Response, ReactiveHttpOutputMessage> bodyInserter = null;
        if (series == HttpStatus.Series.CLIENT_ERROR) {
            bodyInserter = BodyInserters.fromValue(Response.buildFailure(String.valueOf(statusCode), error.get("error").toString()));
        } else {
            bodyInserter = BodyInserters.fromValue(Response.buildFailure(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), error.get("error").toString()));
        }

        return ServerResponse.status(getHttpStatus(error))
                .contentType(MediaType.APPLICATION_JSON)
                .body(bodyInserter);
    }
}
