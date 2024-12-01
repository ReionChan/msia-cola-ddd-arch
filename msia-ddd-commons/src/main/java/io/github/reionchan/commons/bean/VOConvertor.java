package io.github.reionchan.commons.bean;

/**
 * @author Reion
 * @date 2024-11-16
 **/
public interface VOConvertor<VO, DTO> {

    DTO toDTO(VO vo);

    VO toVO(DTO dto);
}
