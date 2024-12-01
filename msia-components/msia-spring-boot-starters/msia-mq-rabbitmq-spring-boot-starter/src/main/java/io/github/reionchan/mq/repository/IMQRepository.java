package io.github.reionchan.mq.repository;

import io.github.reionchan.mq.model.entity.MQMessage;

/**
 * @author Reion
 * @date 2024-11-22
 **/
public interface IMQRepository {
    MQMessage getById(String id);
    boolean updateById(MQMessage mqMessage);

    boolean save(MQMessage registerMsg);
}
