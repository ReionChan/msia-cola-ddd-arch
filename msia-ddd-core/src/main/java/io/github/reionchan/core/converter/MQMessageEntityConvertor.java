package io.github.reionchan.core.converter;

import io.github.reionchan.commons.bean.EntityConvertor;
import io.github.reionchan.core.database.dataobject.MQMessageDO;
import io.github.reionchan.mq.model.entity.MQMessage;
import org.mapstruct.*;

import java.util.Date;

import static io.github.reionchan.core.util.CommonUtil.isEmpty;
import static io.github.reionchan.core.util.CommonUtil.isNotEmpty;

/**
 * @author Reion
 * @date 2024-11-21
 **/
@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MQMessageEntityConvertor extends EntityConvertor<MQMessage, MQMessageDO> {

//    @AfterMapping
//    default void updateDOFromEntity(MQMessage entity, @MappingTarget MQMessageDO dataObject) {
//        if (isNotEmpty(entity.getMessageId())) {
//            if(isEmpty(dataObject.getCreateTime())) {
//                dataObject.setCreateTime(new Date());
//            }
//            dataObject.setUpdateTime(new Date());
//        } else {
//            dataObject.setCreateTime(new Date());
//            dataObject.setUpdateTime(dataObject.getCreateTime());
//        }
//    }
}
