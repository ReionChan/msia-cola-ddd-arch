package io.github.reionchan.users.web;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import io.github.reionchan.users.api.IUserService;
import io.github.reionchan.users.dto.UserDTO;
import io.github.reionchan.users.dto.clientobject.TokenWebCO;
import io.github.reionchan.users.dto.clientobject.UserWebCO;
import io.github.reionchan.users.dto.command.TokenRefreshCmd;
import io.github.reionchan.users.dto.command.UserAddCmd;
import io.github.reionchan.users.dto.command.UserModifyCmd;
import io.github.reionchan.users.dto.command.query.UserByIdQry;
import io.github.reionchan.users.dto.command.query.UserLoginQry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static io.github.reionchan.springdoc.config.SpringDocConfig.RESPONSE_ERROR;
import static io.github.reionchan.springdoc.config.SpringDocConfig.RESPONSE_FAIL;

/**
 * 用户控制器
 *
 * @author Reion
 * @date 2023-12-14
 **/
@RestController
@Tag(name = "UserController", description = "用户服务请求端点")
public class UserController {

    private IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/register")
    @Operation(
        summary = "用户注册", description = "用户注册接口",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "UserDto 传输对象",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = UserAddCmd.class,
                requiredProperties = {"userName", "password"}))),
        responses = {@ApiResponse(
            responseCode = "200",
            description = "成功消息",
            content = {@Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Response.class))}),
            @ApiResponse(responseCode = "400", ref = RESPONSE_FAIL),
            @ApiResponse(responseCode = "500", ref = RESPONSE_ERROR),
        }
    )
    public Response register(@RequestBody @Validated UserAddCmd userAddCmd) {
        return userService.register(userAddCmd);
    }

    @PostMapping("/user/login")
    @Operation(
            summary = "用户登录", description = "用户用户登录接口",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "UserDto 传输对象",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserLoginQry.class,
                                    requiredProperties = {"userName", "password"}))),
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "成功消息",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SingleResponse.class))}),
                    @ApiResponse(responseCode = "400", ref = RESPONSE_FAIL),
                    @ApiResponse(responseCode = "500", ref = RESPONSE_ERROR),
            }
    )
    public SingleResponse<TokenWebCO> login(@RequestBody @Validated UserLoginQry userLoginQry) {
        return userService.login(userLoginQry);
    }

    @Operation(
            summary = "用户刷新令牌", description = "用户刷新令牌接口",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "TokenDto 传输对象",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TokenRefreshCmd.class))),
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "成功消息",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SingleResponse.class))}),
                    @ApiResponse(responseCode = "400", ref = RESPONSE_FAIL),
                    @ApiResponse(responseCode = "500", ref = RESPONSE_ERROR),
            }
    )
    @PostMapping("/user/refresh-token")
    public SingleResponse<TokenWebCO> refreshToken(@RequestBody @Validated TokenRefreshCmd tokenRefreshCmd) {
        return userService.refreshToken(tokenRefreshCmd);
    }

    @Operation(
        summary = "用户修改", description = "支持 userName、password 属性修改",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "UserDto 传输对象",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = UserModifyCmd.class,
                requiredProperties = {"id"})))
    )
    @PutMapping("/user")
    @PreAuthorize("hasRole('user') && principal.id == #userModifyCmd.id")
    public Response modify(@RequestBody @Validated UserModifyCmd userModifyCmd) {
        return userService.modify(userModifyCmd);
    }

    @Operation(
            summary = "用户 ID 查询用户", description = "用户 ID 需为正数",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "UserDto 传输对象",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserDTO.class,
                            requiredProperties = {"id"})))
    )
    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('user') && principal.id == #id")
    public SingleResponse<UserWebCO> getUser(@PathVariable("id") @Validated @Positive(message = "id 必须大于 0")  Long id) {
        UserByIdQry userByIdQry = UserByIdQry.builder().id(id).build();
        return userService.getUserById(userByIdQry);
    }
}
