package io.github.reionchan.payment.convertor;

import io.github.reionchan.commons.bean.EntityConvertor;
import io.github.reionchan.payment.database.dataobject.WalletDO;
import io.github.reionchan.payment.model.entity.Wallet;
import org.mapstruct.*;

import java.util.Date;

import static io.github.reionchan.commons.validation.CommonUtil.isNotEmpty;

/**
 * @author Reion
 * @date 2024-11-21
 **/
@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WalletEntityConvertor extends EntityConvertor<Wallet, WalletDO> {

    @Override
    default void updateDOFromEntity(Wallet wallet, @MappingTarget WalletDO dataObject) {
        if(isNotEmpty(wallet.getId())) {
            dataObject.setUpdateTime(new Date());
        } else {
            dataObject.setCreateTime(new Date());
            dataObject.setUpdateTime(dataObject.getCreateTime());
        }
    }
}
