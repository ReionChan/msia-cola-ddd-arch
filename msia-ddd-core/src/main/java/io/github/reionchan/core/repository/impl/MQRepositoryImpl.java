package io.github.reionchan.core.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.reionchan.core.converter.MQMessageEntityConvertor;
import io.github.reionchan.core.database.dataobject.MQMessageDO;
import io.github.reionchan.core.database.mapper.MQMessageMapper;
import io.github.reionchan.core.model.entity.MQMessage;
import io.github.reionchan.core.repository.IMQRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author Reion
 * @date 2024-11-22
 **/
@Repository
public class MQRepositoryImpl extends ServiceImpl<MQMessageMapper, MQMessageDO> implements IMQRepository {

    @Resource
    private MQMessageMapper mqMessageMapper;
    @Resource
    private MQMessageEntityConvertor messageEntityConvertor;

    @Override
    public MQMessage getById(String id) {
        MQMessageDO messageDO = mqMessageMapper.selectById(id);
        return messageEntityConvertor.toEntity(messageDO);
    }

    @Override
    public boolean updateById(MQMessage mqMessage) {
        mqMessage.setUpdateTime(new Date());
        return this.saveOrUpdate(messageEntityConvertor.toDataObject(mqMessage));
    }

    @Override
    public boolean save(MQMessage msg) {
        msg.setCreateTime(new Date());
        msg.setUpdateTime(msg.getCreateTime());
        return this.saveOrUpdate(messageEntityConvertor.toDataObject(msg));
    }

}
