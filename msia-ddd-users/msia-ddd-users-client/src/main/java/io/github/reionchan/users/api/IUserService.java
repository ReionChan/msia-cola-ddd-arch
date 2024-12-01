package io.github.reionchan.users.api;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import io.github.reionchan.users.dto.clientobject.TokenWebCO;
import io.github.reionchan.users.dto.clientobject.UserWebCO;
import io.github.reionchan.users.dto.command.TokenRefreshCmd;
import io.github.reionchan.users.dto.command.UserAddCmd;
import io.github.reionchan.users.dto.command.UserModifyCmd;
import io.github.reionchan.users.dto.command.query.UserByIdQry;
import io.github.reionchan.users.dto.command.query.UserLoginQry;

/**
 * 用户服务接口
 *
 * @author Reion
 * @date 2023-12-14
 **/
public interface IUserService {

    Response register(UserAddCmd userAddCmd);

    Response modify(UserModifyCmd userModifyCmd);

    SingleResponse<UserWebCO> getUserById(UserByIdQry userByIdQry);

    SingleResponse<TokenWebCO> login(UserLoginQry userLoginQry);

    SingleResponse<TokenWebCO> refreshToken(TokenRefreshCmd tokenRefreshCmd);
}
