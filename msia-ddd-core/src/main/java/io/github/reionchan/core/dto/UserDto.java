package io.github.reionchan.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;

/**
 * 用户数据传输对象
 *
 * @author Reion
 * @date 2023-12-14
 **/
@Data
@Schema(name = "UserDto", description = "用户客户端SDK传输对象")
public class UserDto {

    @NotNull(message = "id 不能为空")
    @Positive(message = "id 必须大于 0")
    @Schema(title = "用户ID", description = "用户唯一ID")
    private Long id;

    @NotNull(message = "用户名不能为空")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]{7,31}$", message = "用户名只能包含字母、下划线，开头为字母，长度 [8, 32] 个字符")
    @Schema(title = "用户名", description = "长度8~32，开头为字母、下划线，之后数字、字母、下划线",  minLength = 8, maxLength = 32)
    private String userName;

    @NotNull(message = "密码不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_.\\-]{8,64}$", message = "密码只能包含字母、数字、下划线、点、连字符，长度 [8, 64] 个字符")
    @Schema(title = "密码", description = "长度8~64",  minLength = 8, maxLength = 64)
    private String password;

    @Schema(title = "用户角色权限")
    private String roles;

    @Min(value = -1, message = "状态值不合法，最小 -1")
    @Max(value = 127, message = "状态值不合法，最大 127")
    @Schema(type = "integer", title = "状态：0、null-启用 -1-停用")
    private Byte status;

    @Schema(title = "创建时间", description = "格式：yyyy-MM-dd hh:mm:ss")
    private Date createTime;

    @Schema(title = "修改时间", description = "格式：yyyy-MM-dd hh:mm:ss")
    private Date updateTime;
}
