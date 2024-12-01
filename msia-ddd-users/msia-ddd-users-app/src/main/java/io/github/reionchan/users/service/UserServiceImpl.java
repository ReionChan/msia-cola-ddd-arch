package io.github.reionchan.users.service;

import com.alibaba.cola.catchlog.CatchAndLog;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import io.github.reionchan.users.api.IUserService;
import io.github.reionchan.users.command.TokenRefreshCmdExe;
import io.github.reionchan.users.command.UserAddCmdExe;
import io.github.reionchan.users.command.UserModifyCmdExe;
import io.github.reionchan.users.command.query.UserByIdQryExe;
import io.github.reionchan.users.command.query.UserLoginQryExe;
import io.github.reionchan.users.dto.clientobject.TokenWebCO;
import io.github.reionchan.users.dto.clientobject.UserWebCO;
import io.github.reionchan.users.dto.command.TokenRefreshCmd;
import io.github.reionchan.users.dto.command.UserAddCmd;
import io.github.reionchan.users.dto.command.UserModifyCmd;
import io.github.reionchan.users.dto.command.query.UserByIdQry;
import io.github.reionchan.users.dto.command.query.UserLoginQry;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务实现
 *
 * @author Reion
 * @date 2023-12-14
 **/
@Slf4j
@Service
@CatchAndLog
public class UserServiceImpl implements IUserService {

    @Resource
    private UserAddCmdExe userAddCmdExe;
    @Resource
    private UserLoginQryExe userLoginQryExe;
    @Resource
    private UserByIdQryExe userByIdQryExe;
    @Resource
    private UserModifyCmdExe userModifyCmdExe;
    @Resource
    private TokenRefreshCmdExe tokenRefreshCmdExe;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response register(UserAddCmd userAddCmd) {
        return userAddCmdExe.execute(userAddCmd);
    }

    @Override
    public Response modify(UserModifyCmd userModifyCmd) {
        return userModifyCmdExe.execute(userModifyCmd);
    }

    @Override
    public SingleResponse<UserWebCO> getUserById(UserByIdQry userByIdQry) {
        return userByIdQryExe.execute(userByIdQry);
    }

    @Override
    public SingleResponse<TokenWebCO> login(UserLoginQry userLoginQry) {
        return userLoginQryExe.execute(userLoginQry);
    }

    @Override
    public SingleResponse<TokenWebCO> refreshToken(TokenRefreshCmd tokenRefreshCmd) {
        return tokenRefreshCmdExe.execute(tokenRefreshCmd);
    }
}
