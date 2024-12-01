package io.github.reionchan.core.config;

import com.alibaba.cola.catchlog.DefaultResponseHandler;
import com.alibaba.cola.catchlog.ResponseHandlerI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Reion
 * @date 2024-11-23
 **/
@Configuration
public class CatchLogConfiguration {

    @Bean
    public ResponseHandlerI responseHandlerI() {
        return new DefaultResponseHandler();
    }
}
