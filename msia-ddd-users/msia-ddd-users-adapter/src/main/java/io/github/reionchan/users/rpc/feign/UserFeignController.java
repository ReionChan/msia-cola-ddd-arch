package io.github.reionchan.users.rpc.feign;

import com.alibaba.cola.dto.SingleResponse;
import io.github.reionchan.users.api.IUserService;
import io.github.reionchan.users.dto.UserDTO;
import io.github.reionchan.users.dto.clientobject.UserWebCO;
import io.github.reionchan.users.dto.command.query.UserByIdQry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户 Feign RPC 控制器
 *
 * @author Reion
 * @date 2023-12-14
 **/
@RestController
@Tag(name = "UserFeignController", description = "用户 Feign RPC 控制器请求端点")
public class UserFeignController {

    private IUserService userService;

    public UserFeignController(IUserService userService) {
        this.userService = userService;
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
    @GetMapping("/rpc/user/{id}")
    @PreAuthorize("hasRole('admin')")
    public SingleResponse<UserWebCO> getUser(@PathVariable("id") @Validated @Positive(message = "id 必须大于 0")  Long id) {
        UserByIdQry userByIdQry = UserByIdQry.builder().id(id).build();
        return userService.getUserById(userByIdQry);
    }
}
