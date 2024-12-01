package io.github.reionchan.users.model.entity;

import com.alibaba.cola.domain.Entity;
import io.github.reionchan.users.model.vo.RoleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 角色域
 *
 * @author Reion
 * @date 2023-04-25
 **/
@Slf4j
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "roleName"})
public class Role implements Serializable {

    static final long serialVersionUID = 1L;

    private Long id;

    private String roleName;

    private RoleStatus status;
}
