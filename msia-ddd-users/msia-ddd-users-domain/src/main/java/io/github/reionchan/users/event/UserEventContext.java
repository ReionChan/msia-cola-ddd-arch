package io.github.reionchan.users.event;

import io.github.reionchan.users.model.entity.User;
import lombok.Data;

/**
 * @author Reion
 * @date 2024-11-29
 **/
@Data
public class UserEventContext {
    private User user;
}
