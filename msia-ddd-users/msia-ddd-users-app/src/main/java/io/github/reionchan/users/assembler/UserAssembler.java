package io.github.reionchan.users.assembler;

import io.github.reionchan.commons.bean.DTOAssembler;
import io.github.reionchan.core.dto.UserDto;
import io.github.reionchan.users.dto.UserDTO;
import io.github.reionchan.users.dto.clientobject.UserWebCO;
import io.github.reionchan.users.dto.command.UserAddCmd;
import io.github.reionchan.users.dto.command.UserModifyCmd;
import io.github.reionchan.users.model.entity.User;
import io.github.reionchan.users.database.dataobject.UserDO;
import io.github.reionchan.users.model.vo.UserStatus;
import org.mapstruct.*;

import java.util.List;

import static io.github.reionchan.commons.validation.CommonUtil.*;


/**
 * @author Reion
 * @date 2024-11-16
 **/
@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserAssembler extends DTOAssembler<UserDTO, User> {

    @AfterMapping
    default void afterToEntity(UserDTO source, @MappingTarget User user) {
        String roles = isNotEmpty(source.getRoles()) ? source.getRoles() : "user";
        user.getRoleList(roles);
    }

    UserDto toJwtDto(User user);

    UserWebCO toWebCO(User user);

    List<UserDTO> toDTOList(List<UserDO> dataObjectList);

    User toEntity(UserAddCmd cmd);

    @AfterMapping
    default void afterToEntity(UserAddCmd source, @MappingTarget User user) {
        String roles = isNotEmpty(source.getRoles()) ? source.getRoles() : "user";
        user.getRoleList(roles);
    }

    User toEntity(UserModifyCmd cmd);

}
