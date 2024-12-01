package io.github.reionchan.users.model.entity;

import com.alibaba.cola.domain.ApplicationContextHelper;
import com.alibaba.cola.domain.Entity;
import com.alibaba.cola.exception.Assert;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.StateMachineFactory;
import io.github.reionchan.users.event.UserEvent;
import io.github.reionchan.users.event.UserEventContext;
import io.github.reionchan.users.gateway.IUserGateway;
import io.github.reionchan.users.event.UserEventType;
import io.github.reionchan.users.model.vo.UserStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import static io.github.reionchan.users.event.UserStateMachine.USER_STATE_MACHINE_ID;
import static io.github.reionchan.users.event.UserEventType.*;
import static io.github.reionchan.users.model.vo.UserStatus.*;
import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * 用户域
 *
 * @author Reion
 * @date 2023-04-25
 **/
@Slf4j
@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "userName"})
public class User implements Serializable {

    public static final String SPLIT = ",";

    private Long id;

    private String userName;

    private String password;

    private List<String> roleList;

    private UserStatus userStatus = INIT;

    private IUserGateway userGateway = ApplicationContextHelper.getBean(IUserGateway.class);

    private StateMachine<UserStatus, UserEventType, UserEventContext> userStateMachine =
            StateMachineFactory.get(USER_STATE_MACHINE_ID);

    public void getRoleList(String roles) {
        Assert.isTrue(roles != null && !roles.isBlank(), "roles 需不为空");
        this.roleList = Arrays.asList(roles.split(SPLIT));
    }

    public String getRoles() {
        Assert.notNull(roleList, "角色列表不能为空");
        return String.join(SPLIT, roleList);
    }

    public void getUserStatus(Byte status) {
        Assert.notNull(status, "status 需不为空");
        this.userStatus = UserStatus.of(status);
    }

    public Byte getStatus() {
        Assert.notNull(userStatus, "userStatus 需不为空");
        return userStatus.getValue();
    }

    public boolean isEnabled() {
        return ENABLED.equals(userStatus);
    }

    /**
     * 用户注册
     */
    public boolean register() {
        UserEventContext ctx = new UserEventContext();
        ctx.setUser(this);
        UserEvent event = new UserEvent(USER_REGISTER, ctx);
        this.fireEvent(event);
        userGateway.save(this);
        return !isEmpty(this.id) && this.id > 0;
    }

    /**
     * 用户登录
     */
    public boolean login(String password) {
        Assert.isTrue(isEnabled(), "用户已停用");
        return this.password.equals(password);
    }

    /**
     * 用户修改资料：用户名或密码
     */
    public boolean updateProfile(String userName, String password) {
        boolean needUpdate = false;
        if(!isEmpty(userName) && !this.userName.equals(userName)) {
            this.userName = userName;
            needUpdate = true;
        }
        if(!isEmpty(password) && !this.password.equals(password)) {
            this.password = password;
            needUpdate = true;
        }
        // 用户名密码都相同，直接返回
        if (!needUpdate) {
            return true;
        }
        return userGateway.update(this);
    }

    /**
     * 用户禁用
     */
    public boolean deactivate() {
        UserEventContext ctx = new UserEventContext();
        ctx.setUser(this);
        UserEvent event = new UserEvent(USER_DISABLE, ctx);
        UserStatus status = this.fireEvent(event);
        userGateway.update(this);
        return DISABLED.equals(status);
    }

    /**
     * 用户启用
     */
    public boolean activate() {
        UserEventContext ctx = new UserEventContext();
        ctx.setUser(this);
        UserEvent event = new UserEvent(USER_ENABLE, ctx);
        UserStatus status = this.fireEvent(event);
        userGateway.update(this);
        return ENABLED.equals(status);
    }

    /**
     * 状态机触发事件
     */
    private UserStatus fireEvent(UserEvent event) {
        return userStateMachine.fireEvent(userStatus, event.getEventType(), (UserEventContext) event.getSource());
    }
}
