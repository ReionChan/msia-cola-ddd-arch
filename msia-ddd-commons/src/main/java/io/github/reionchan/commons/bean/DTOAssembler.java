package io.github.reionchan.commons.bean;

/**
 * @author Reion
 * @date 2024-11-16
 **/
public interface DTOAssembler<DTO, Entity> {

    Entity toEntity(DTO dto);

    DTO toDTO(Entity entity);
}
