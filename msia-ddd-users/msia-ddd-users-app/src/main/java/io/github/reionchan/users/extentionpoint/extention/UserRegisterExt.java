package io.github.reionchan.users.extentionpoint.extention;

import com.alibaba.cola.exception.Assert;
import com.alibaba.cola.extension.Extension;
import com.alibaba.fastjson.JSON;
import io.github.reionchan.core.mq.MQManager;
import io.github.reionchan.mq.model.entity.MQMessage;
import io.github.reionchan.users.dto.command.UserAddCmd;
import io.github.reionchan.users.extentionpoint.UserRegisterExtPt;
import io.github.reionchan.users.repository.IRoleRepository;
import io.github.reionchan.users.repository.IUserRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;

import static io.github.reionchan.commons.validation.CommonUtil.isNotEmpty;
import static io.github.reionchan.users.consts.RabbitMQConst.USER_REGISTER_EXCHANGE;
import static io.github.reionchan.users.consts.RabbitMQConst.USER_REGISTER_ROUTING_KEY;
import static io.github.reionchan.users.consts.UsersBizScenarioCst.USER_BIZ_ID;
import static io.github.reionchan.users.consts.UsersBizScenarioCst.USER_REGISTER_USE_CASE;

/**
 * @author Reion
 * @date 2024-11-20
 **/
@Slf4j
@Extension(bizId = USER_BIZ_ID, useCase = USER_REGISTER_USE_CASE)
public class UserRegisterExt implements UserRegisterExtPt {

    @Resource
    private IUserRepository userRepository;

    @Resource
    private IRoleRepository roleRepository;

    @Resource
    private MQManager mqManager;

    @Override
    public void validate(UserAddCmd cmd) {
        // 校验用户名是否存在
        Assert.isTrue(!userRepository.existsUserName(cmd.getUserName()), cmd.getUserName() + " 已存在");

        // 校验角色名是否存在
        String roleStr = isNotEmpty(cmd.getRoles()) ? cmd.getRoles() : "user";
        Assert.isTrue(roleRepository.allRolesExist(roleStr), "角色不存在");
    }

    @Override
    public void notify(MQMessage message) {
        log.info(JSON.toJSONString(message));
        mqManager.sendMessage(USER_REGISTER_EXCHANGE, USER_REGISTER_ROUTING_KEY, message, new CorrelationData(message.getMessageId()));
    }
}
