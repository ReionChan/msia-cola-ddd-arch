package io.github.reionchan.users.command.query;

import com.alibaba.cola.dto.SingleResponse;
import com.alibaba.cola.exception.Assert;
import com.alibaba.cola.extension.ExtensionExecutor;
import io.github.reionchan.users.assembler.UserAssembler;
import io.github.reionchan.users.dto.clientobject.UserWebCO;
import io.github.reionchan.users.dto.command.query.UserByIdQry;
import io.github.reionchan.users.model.entity.User;
import io.github.reionchan.users.repository.IUserRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Reion
 * @date 2024-11-16
 **/
@Slf4j
@Component
public class UserByIdQryExe {

    @Resource
    private IUserRepository userRepository;
    @Resource
    private UserAssembler userAssembler;

    public SingleResponse<UserWebCO> execute(UserByIdQry qry) {
        // 校验
        // 查询
        User user = userRepository.queryById(qry.getId());
        Assert.notNull(user, "用户不存在");
        return SingleResponse.of(userAssembler.toWebCO(user));
    }
}
