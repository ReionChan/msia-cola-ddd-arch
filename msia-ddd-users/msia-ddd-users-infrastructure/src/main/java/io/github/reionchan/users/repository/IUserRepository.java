package io.github.reionchan.users.repository;

import io.github.reionchan.users.model.entity.User;

/**
 * @author Reion
 * @date 2024-11-21
 **/
public interface IUserRepository {
    boolean save(User user);
    boolean existsUserName(String userName);

    User queryByUserName(String userName);

    User queryById(Long id);

    boolean modify(User user);
}
