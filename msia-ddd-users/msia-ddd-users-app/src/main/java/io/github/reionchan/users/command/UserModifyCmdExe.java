package io.github.reionchan.users.command;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.Assert;
import com.alibaba.cola.extension.ExtensionExecutor;
import io.github.reionchan.users.dto.command.UserModifyCmd;
import io.github.reionchan.users.extentionpoint.UserModifyExtPt;
import io.github.reionchan.users.model.entity.User;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static io.github.reionchan.commons.validation.CommonUtil.isEmpty;

/**
 * @author Reion
 * @date 2024-11-16
 **/
@Slf4j
@Component
public class UserModifyCmdExe {

    @Resource
    private ExtensionExecutor extensionExecutor;

    public Response execute(UserModifyCmd cmd) {
        if (isEmpty(cmd.getUserName()) && isEmpty(cmd.getPassword())) {
            return Response.buildSuccess();
        }
        // 校验
        User user = extensionExecutor.execute(UserModifyExtPt.class, cmd.getBizScenario(), chk -> chk.exists(cmd));
        // 用户修改用户名或密码
        Assert.isTrue(user.updateProfile(cmd.getUserName(), cmd.getPassword()), "用户修改资料失败");
        return Response.buildSuccess();
    }
}
