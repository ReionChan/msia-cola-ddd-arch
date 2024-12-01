package io.github.reionchan.users.gateway;

import io.github.reionchan.users.model.entity.User;
import io.github.reionchan.users.repository.impl.UserRepositoryImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @author Reion
 * @date 2024-11-21
 **/
@Component
public class UserGatewayImpl implements IUserGateway {

    @Resource
    private UserRepositoryImpl userRepository;

    @Override
    public boolean save(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean update(User user) {
        return userRepository.modify(user);
    }
}
