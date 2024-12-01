package io.github.reionchan.users.convertor;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import io.github.reionchan.mq.consts.MessageStatus;
import io.github.reionchan.mq.model.entity.MQMessage;
import io.github.reionchan.users.assembler.UserAssembler;
import io.github.reionchan.users.dto.UserDTO;
import io.github.reionchan.users.model.entity.User;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Date;

import static io.github.reionchan.users.consts.RabbitMQConst.USER_REGISTER_EXCHANGE;
import static io.github.reionchan.users.consts.RabbitMQConst.USER_REGISTER_ROUTING_KEY;

/**
 * @author Reion
 * @date 2024-11-22
 **/
@Component
public class User2MessageConvertor {

    @Resource
    private UserAssembler userAssembler;

    public MQMessage user2Message(User user) {
        return MQMessage.builder()
                .messageId(IdUtil.simpleUUID())
                .content(JSON.toJSONString(userAssembler.toDTO(user)))
                .toExchange(USER_REGISTER_EXCHANGE)
                .routingKey(USER_REGISTER_ROUTING_KEY)
                .classType(UserDTO.class.getName())
                .messageStatus(MessageStatus.NEW.getValue())
                .createTime(new Date())
                .updateTime(new Date())
                .build();
    }
}
