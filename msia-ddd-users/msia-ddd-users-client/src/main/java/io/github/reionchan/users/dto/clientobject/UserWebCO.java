package io.github.reionchan.users.dto.clientobject;

import com.alibaba.cola.dto.ClientObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

/**
 * 用户数据传输对象
 *
 * @author Reion
 * @date 2023-12-14
 **/
@Data
@Schema(name = "UserWebCO", description = "用户客户端SDK传输对象")
public class UserWebCO extends ClientObject {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "id 不能为空")
    @Positive(message = "id 必须大于 0")
    @Schema(title = "用户ID", description = "用户唯一ID")
    private Long id;

    @NotNull(message = "用户名不能为空")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]{7,31}$", message = "用户名只能包含字母、下划线，开头为字母，长度 [8, 32] 个字符")
    @Schema(title = "用户名", description = "长度8~32，开头为字母、下划线，之后数字、字母、下划线",  minLength = 8, maxLength = 32)
    private String userName;

    @Min(value = -1, message = "状态值不合法，最小 -1")
    @Max(value = 127, message = "状态值不合法，最大 127")
    @Schema(type = "integer", title = "状态：0、null-启用 -1-停用")
    private Byte status;

    private List<String> roleList;
}
