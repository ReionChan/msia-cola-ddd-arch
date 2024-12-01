package io.github.reionchan.users.event.condition;

import com.alibaba.cola.statemachine.Condition;
import io.github.reionchan.users.event.UserEventContext;
import io.github.reionchan.users.model.entity.User;

import static io.github.reionchan.users.model.vo.UserStatus.*;
import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * @author Reion
 * @date 2024-11-29
 **/
public class UserRegisterCondition implements Condition<UserEventContext> {
    @Override
    public boolean isSatisfied(UserEventContext context) {
        User user = context.getUser();
        return INIT.equals(user.getUserStatus()) && isEmpty(user.getId());
    }
}
