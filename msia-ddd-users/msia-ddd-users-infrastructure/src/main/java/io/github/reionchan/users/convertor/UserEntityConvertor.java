package io.github.reionchan.users.convertor;

import io.github.reionchan.commons.bean.EntityConvertor;
import io.github.reionchan.users.database.dataobject.UserDO;
import io.github.reionchan.users.model.entity.User;
import org.mapstruct.*;

/**
 * @author Reion
 * @date 2024-11-21
 **/
@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserEntityConvertor extends EntityConvertor<User, UserDO> {

    @AfterMapping
    default void afterToEntity(UserDO source, @MappingTarget User user) {
        user.getRoleList(source.getRoles());
        user.getUserStatus(source.getStatus());
    }
}
