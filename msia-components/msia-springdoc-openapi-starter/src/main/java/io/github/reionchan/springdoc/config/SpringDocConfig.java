package io.github.reionchan.springdoc.config;

import io.github.reionchan.springdoc.openapi.ReadResponseJson;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 文档配置类
 *
 * @author Reion
 * @date 2023-06-06
 **/
@Configuration
@OpenAPIDefinition
public class SpringDocConfig {

    public static final String APPLICATION_JSON_VALUE = "application/json";

    /**
     * 客户端异常响应引用名称
     */
    public static final String RESPONSE_FAIL = "failResponse";
    /**
     * 服务端异常响应引用名称
     */
    public static final String RESPONSE_ERROR = "errorResponse";

    public static final String RESPONSE_NOT_ALLOW = "notAllowResponse";

    /**
     * Swagger UI 接口测试 - 网关服务器地址
     */
    @Value("${springdoc.gateway.url:http://localhost:9000}")
    private String gatewayUrl;

    /**
     * Swagger UI 接口测试 - 网关负载均衡路由时指定的服务名
     */
    @Value("${spring.application.name}")
    private String appName;

    /**
     * Swagger UI 显示的应用版本信息 - 来源于项目的 Maven 配置版本
     */
    @Value("${maven.version}")
    private String version;

    @Autowired
    private ReadResponseJson readResponseJson;

    @Bean
    // @formatter:off
    public OpenAPI baseOpenAPI()throws IOException {
        Map<String, Object> exampleMap = readResponseJson.read();
        // 定义通用客户端异常返回格式
        ApiResponse failResponse = new ApiResponse().content(
                new Content().addMediaType(APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
                                new Example().value(exampleMap.get(RESPONSE_FAIL))))
        ).description("客户端异常消息");

        // 定义通用客户端异常返回格式
        ApiResponse notAllowResponse = new ApiResponse().content(
                new Content().addMediaType(APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
                                new Example().value(exampleMap.get(RESPONSE_NOT_ALLOW))))
        ).description("客户端异常消息");

        // 定义通用服务端异常返回格式
        ApiResponse errorResponse = new ApiResponse().content(
                new Content().addMediaType(APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
                                new Example().value(exampleMap.get(RESPONSE_ERROR))))
        ).description("服务端异常消息");

        // 通用组件
        Components components = new Components();
        components.addResponses(RESPONSE_FAIL, failResponse);
        components.addResponses(RESPONSE_NOT_ALLOW, notAllowResponse);
        components.addResponses(RESPONSE_ERROR, errorResponse);
        // 全局认证 JWT 令牌请求头设置
        components.addSecuritySchemes("bearerAuth",
                new io.swagger.v3.oas.models.security.SecurityScheme()
                   .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                   .scheme("bearer")
                   .bearerFormat("JWT")
                   .in(io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER)
                   .name("Authorization"));

        return new OpenAPI()
            // 设置 Swagger UI 页面调用测试服务器地址
            .servers(List.of(new Server().url(gatewayUrl + "/" + appName)))
            // 设置 Swagger 文档中通用组件
            .components(components)
            .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList("bearerAuth"))
            .info(new Info()
                .title("微服务实战")
                .description("微服务项目实战示例")
                .version(version)
                // 联系信息
                .contact(new Contact()
                    .name("ReionChan")
                    .email("reion78@gmail.com")
                    .url("https://reionchan.github.io/"))
                    // 许可协议
                    .license(new License()
                        .name("Apache 2.0 license")
                        .url("https://www.apache.org/licenses/LICENSE-2.0")
                        .identifier("Apache-2.0"))
                    // 服务条款的 URL 地址
                    //.termsOfService("/server/termsOfService")
        );
    }
    // @formatter:on
}
