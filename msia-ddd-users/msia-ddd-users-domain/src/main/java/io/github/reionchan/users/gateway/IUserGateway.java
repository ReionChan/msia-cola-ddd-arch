package io.github.reionchan.users.gateway;

import io.github.reionchan.users.model.entity.User;

/**
 * @author Reion
 * @date 2024-11-19
 **/
public interface IUserGateway {
    boolean save(User user);

    boolean update(User user);
}
