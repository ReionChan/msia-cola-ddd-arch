package io.github.reionchan.users.event.action;

import com.alibaba.cola.statemachine.Action;
import io.github.reionchan.users.event.UserEventContext;
import io.github.reionchan.users.model.entity.User;
import io.github.reionchan.users.event.UserEventType;
import io.github.reionchan.users.model.vo.UserStatus;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Reion
 * @date 2024-11-30
 **/
@Slf4j
public class UserRegisterAction implements Action<UserStatus, UserEventType, UserEventContext> {

    @Override
    public void execute(UserStatus from, UserStatus to, UserEventType event, UserEventContext context) {
        log.debug("{} has been initialized and activated!", context.getUser().getUserName());
        User user = context.getUser();
        user.setUserStatus(UserStatus.ENABLED);
    }
}
