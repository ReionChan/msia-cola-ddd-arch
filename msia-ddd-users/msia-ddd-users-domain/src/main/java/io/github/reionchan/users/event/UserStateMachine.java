package io.github.reionchan.users.event;

import com.alibaba.cola.exception.Assert;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import io.github.reionchan.users.event.action.UserDisableAction;
import io.github.reionchan.users.event.action.UserEnableAction;
import io.github.reionchan.users.event.action.UserRegisterAction;
import io.github.reionchan.users.event.condition.UserDisableCondition;
import io.github.reionchan.users.event.condition.UserEnableCondition;
import io.github.reionchan.users.event.condition.UserRegisterCondition;
import io.github.reionchan.users.model.vo.UserStatus;

import static io.github.reionchan.users.event.UserEventType.*;
import static io.github.reionchan.users.model.vo.UserStatus.*;

/**
 * @author Reion
 * @date 2024-11-29
 **/
public class UserStateMachine {
    public static final String USER_STATE_MACHINE_ID = "userStateMachine";

    public UserStateMachine() {
        this.init();
    }

    private void init() {
        StateMachineBuilder<UserStatus, UserEventType, UserEventContext> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
                .from(INIT)
                .to(ENABLED)
                .on(USER_REGISTER)
                .when(new UserRegisterCondition())
                .perform(new UserRegisterAction());

        builder.externalTransition()
                .from(DISABLED)
                .to(ENABLED)
                .on(USER_ENABLE)
                .when(new UserEnableCondition())
                .perform(new UserEnableAction());

        builder.externalTransition()
                .from(ENABLED)
                .to(DISABLED)
                .on(USER_DISABLE)
                .when(new UserDisableCondition())
                .perform(new UserDisableAction());

        builder.setFailCallback((s, e, c) -> {
            Assert.isTrue(false, s.getName() + " 不满足 " + e.name() + " 前置要求！");
        });

        builder.build(USER_STATE_MACHINE_ID);
    }
}
