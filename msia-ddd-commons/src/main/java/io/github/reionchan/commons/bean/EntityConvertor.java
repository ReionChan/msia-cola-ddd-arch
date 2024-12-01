package io.github.reionchan.commons.bean;

import org.mapstruct.MappingTarget;

/**
 * @author Reion
 * @date 2024-11-16
 **/
public interface EntityConvertor<Entity, DO> {

    Entity toEntity(DO dataObject);

    DO toDataObject(Entity entity);

    void updateDOFromEntity(Entity entity, @MappingTarget DO dataObject);
}
