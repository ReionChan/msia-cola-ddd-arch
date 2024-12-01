package io.github.reionchan.users.dto.command;

import com.alibaba.cola.dto.Command;
import com.alibaba.cola.extension.BizScenario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import static io.github.reionchan.users.consts.UsersBizScenarioCst.*;

/**
 * @author Reion
 * @date 2024-11-16
 **/
@Data
@Schema(name = "UserModifyCmd", description = "用户修改命令对象")
public class UserModifyCmd extends Command {

    private static final long serialVersionUID = 1L;

    private BizScenario bizScenario = BizScenario.valueOf(USER_BIZ_ID, USER_MODIFY_USER_CASE);

    @NotNull(message = "id 不能为空")
    @Positive(message = "id 必须大于 0")
    @Schema(title = "用户ID", description = "用户唯一ID")
    private Long id;

    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]{7,31}$", message = "用户名只能包含字母、下划线，开头为字母，长度 [8, 32] 个字符")
    @Schema(title = "用户名", description = "长度8~32，开头为字母、下划线，之后数字、字母、下划线",  minLength = 8, maxLength = 32)
    private String userName;

    @Pattern(regexp = "^[a-zA-Z0-9_.\\-]{8,64}$", message = "密码只能包含字母、数字、下划线、点、连字符，长度 [8, 64] 个字符")
    @Schema(title = "密码", description = "长度8~64",  minLength = 8, maxLength = 64)
    private String password;
}
