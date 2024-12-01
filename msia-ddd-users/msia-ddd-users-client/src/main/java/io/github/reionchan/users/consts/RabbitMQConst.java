package io.github.reionchan.users.consts;

/**
 * @author Reion
 * @date 2024-07-05
 **/
public interface RabbitMQConst {
    // 用户注册
    String USER_REGISTER_EXCHANGE = "user.register.exchange";
    String USER_REGISTER_QUEUE = "user.register.queue";
    String USER_REGISTER_ROUTING_KEY = "user.register.routing";
    String USER_REGISTER_DLX = "user.register.dlx";
    String USER_REGISTER_DLX_ROUTING_KEY = "user.register.dlx.routing.key";
    String USER_REGISTER_DLQ = USER_REGISTER_QUEUE + ".dlq";
}
