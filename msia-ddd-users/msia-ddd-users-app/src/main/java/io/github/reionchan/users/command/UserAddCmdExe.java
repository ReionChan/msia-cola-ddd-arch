package io.github.reionchan.users.command;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.Assert;
import com.alibaba.cola.extension.ExtensionExecutor;
import io.github.reionchan.core.mq.MQManager;
import io.github.reionchan.mq.model.entity.MQMessage;
import io.github.reionchan.users.assembler.UserAssembler;
import io.github.reionchan.users.convertor.User2MessageConvertor;
import io.github.reionchan.users.dto.command.UserAddCmd;
import io.github.reionchan.users.extentionpoint.UserRegisterExtPt;
import io.github.reionchan.users.model.entity.User;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Reion
 * @date 2024-11-16
 **/
@Slf4j
@Component
public class UserAddCmdExe {

    @Resource
    private UserAssembler userAssembler;
    @Resource
    private ExtensionExecutor extensionExecutor;
    @Resource
    private User2MessageConvertor messageConvertor;
    @Resource
    private MQManager mqManager;

    public Response execute(UserAddCmd cmd) {
        // 校验
        extensionExecutor.executeVoid(UserRegisterExtPt.class, cmd.getBizScenario(), ext -> ext.validate(cmd));
        // 转换
        User user = userAssembler.toEntity(cmd);
        // 用户注册
        Assert.isTrue(user.register(), "用户注册失败");
        // 保存消息
        MQMessage registerMsg = messageConvertor.user2Message(user);
        Assert.isTrue(mqManager.save(registerMsg), "用户注册成功消息保存失败");
        // 发送注册消息
        extensionExecutor.executeVoid(UserRegisterExtPt.class, cmd.getBizScenario(), ext -> ext.notify(registerMsg));
        return Response.buildSuccess();
    }
}
