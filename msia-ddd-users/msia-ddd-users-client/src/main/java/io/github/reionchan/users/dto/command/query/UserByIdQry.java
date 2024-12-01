package io.github.reionchan.users.dto.command.query;

import com.alibaba.cola.dto.Query;
import com.alibaba.cola.extension.BizScenario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

/**
 * @author Reion
 * @date 2024-11-16
 **/
@Data
@Builder
@Schema(name = "UserAddCmd", description = "用户新增命令对象")
public class UserByIdQry extends Query {

    private static final long serialVersionUID = 1L;

    private BizScenario bizScenario;

    @NotNull(message = "id 不能为空")
    @Positive(message = "id 必须大于 0")
    @Schema(title = "用户ID", description = "用户唯一ID")
    private Long id;
}
