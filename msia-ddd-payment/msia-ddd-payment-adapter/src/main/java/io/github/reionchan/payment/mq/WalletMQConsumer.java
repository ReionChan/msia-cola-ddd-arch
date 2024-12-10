package io.github.reionchan.payment.mq;

import com.alibaba.cola.dto.SingleResponse;
import com.alibaba.cola.exception.Assert;
import com.alibaba.cola.exception.BizException;
import com.rabbitmq.client.Channel;
import io.github.reionchan.payment.api.IWalletService;
import io.github.reionchan.payment.dto.command.WalletCreateCmd;
import io.github.reionchan.users.dto.UserDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.io.IOException;
import java.util.function.Consumer;

import static org.springframework.amqp.support.AmqpHeaders.CORRELATION_ID;

/**
 * @author Reion
 * @date 2024-07-05
 **/
@Slf4j
@Configuration
public class WalletMQConsumer {

    @Resource
    private IWalletService walletService;

    @Bean
    public Consumer<Message<UserDTO>> userRegisterConsumer() {
        return message -> {
            long deliveryTag = message.getHeaders().get(AmqpHeaders.DELIVERY_TAG, Long.class);
            Channel channel = message.getHeaders().get(AmqpHeaders.CHANNEL, Channel.class);
            UserDTO userDto = message.getPayload();

            log.info("接收消息：{}", userDto);
            String msgId = message.getHeaders().get(CORRELATION_ID, String.class);
            log.info("correlationId：{} - userId: {} - deliveryTag: {}", msgId, userDto.getId(), deliveryTag);

            WalletCreateCmd cmd = WalletCreateCmd.builder().userId(userDto.getId()).bizScenario(WalletCreateCmd.getDefault()).build();
            try {
                SingleResponse<Long> response = walletService.createWallet(cmd);
                Assert.isTrue(response.getData()>0, "钱包创建失败");
                channel.basicAck(deliveryTag, false);
                log.info("用户注册后钱包创建成功，用户ID：{}", userDto.getId());
            } catch (Exception e) {
                try {
                    channel.basicNack(deliveryTag, false, false);
                } catch (IOException ex) {
                    throw new BizException(cmd.getBizScenario().getUniqueIdentity(), "创建用户钱包失败:" + ex.getMessage());
                }
                throw new BizException(cmd.getBizScenario().getUniqueIdentity(), "创建用户钱包失败:" + e.getMessage());
            }
        };
    }
}
