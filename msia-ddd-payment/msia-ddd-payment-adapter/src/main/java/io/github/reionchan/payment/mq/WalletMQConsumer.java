package io.github.reionchan.payment.mq;

import com.alibaba.cola.dto.SingleResponse;
import com.alibaba.cola.exception.BizException;
import com.alibaba.fastjson2.JSON;
import com.rabbitmq.client.Channel;
import io.github.reionchan.payment.api.IWalletService;
import io.github.reionchan.payment.dto.command.WalletCreateCmd;
import io.github.reionchan.users.dto.UserDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;

import static io.github.reionchan.mq.consts.RabbitMQConst.HEADER_MESSAGE_CORRELATION;
import static io.github.reionchan.users.consts.RabbitMQConst.USER_REGISTER_QUEUE;

/**
 * @author Reion
 * @date 2024-07-05
 **/
@Slf4j
@Component
public class WalletMQConsumer {

    @Resource
    private IWalletService walletService;

    @RabbitListener(queues = USER_REGISTER_QUEUE)
    public void consumer(Channel channel, Message message) throws IOException {
        String msgBodyStr = new String(message.getBody());
        log.info("接收消息：{}", msgBodyStr);
        String msgId = message.getMessageProperties().getHeader(HEADER_MESSAGE_CORRELATION);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        UserDTO userDto = JSON.parseObject(msgBodyStr, UserDTO.class);
        log.info("correlationId：{} - userId: {} - deliveryTag: {}", msgId, userDto.getId(), deliveryTag);

        WalletCreateCmd cmd = WalletCreateCmd.builder().userId(userDto.getId()).bizScenario(WalletCreateCmd.getDefault()).build();
        try {
            SingleResponse<Long> response = walletService.createWallet(cmd);
            Assert.isTrue(response.getData()>0, "钱包创建失败");
            channel.basicAck(deliveryTag, false);
            log.info("用户注册后钱包创建成功，用户ID：{}", userDto.getId());
        } catch (Exception e) {
            channel.basicNack(deliveryTag, false, true);
            throw new BizException(cmd.getBizScenario().getUniqueIdentity(), "创建用户钱包失败:" + e.getMessage());
        }
    }
}
