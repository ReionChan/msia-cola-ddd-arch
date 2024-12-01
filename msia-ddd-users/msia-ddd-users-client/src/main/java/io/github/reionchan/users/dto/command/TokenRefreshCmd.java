package io.github.reionchan.users.dto.command;

import com.alibaba.cola.dto.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户令牌传输对象
 *
 * @author Reion
 * @date 2023-12-14
 **/
@Data
@Schema(name = "TokenRefreshCmd", description = "用户令牌传输对象")
public class TokenRefreshCmd extends DTO {

    @NotNull(message = "访问令牌不能为空")
    @Schema(title = "访问令牌", description = "获得资源访问权限")
    private String accessToken;

    @NotNull(message = "刷新令牌不能为空")
    @Schema(title = "刷新令牌", description = "获取新访问令牌")
    private String refreshToken;
}
