package io.github.reionchan.users.command;

import com.alibaba.cola.dto.SingleResponse;
import com.alibaba.cola.exception.Assert;
import com.alibaba.cola.extension.BizScenario;
import com.alibaba.cola.extension.ExtensionExecutor;
import io.github.reionchan.core.dto.UserDto;
import io.github.reionchan.core.security.JwtUtil;
import io.github.reionchan.users.assembler.UserAssembler;
import io.github.reionchan.users.dto.clientobject.TokenWebCO;
import io.github.reionchan.users.dto.command.TokenRefreshCmd;
import io.github.reionchan.users.extentionpoint.TokenRefreshExtPt;
import io.github.reionchan.users.model.entity.User;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static io.github.reionchan.commons.validation.CommonUtil.isNotEmpty;
import static io.github.reionchan.users.consts.UsersBizScenarioCst.USER_BIZ_ID;
import static io.github.reionchan.users.consts.UsersBizScenarioCst.USER_TOKEN_REFRESH_USER_CASE;

/**
 * @author Reion
 * @date 2024-11-16
 **/
@Slf4j
@Component
public class TokenRefreshCmdExe {

    @Resource
    private ExtensionExecutor extensionExecutor;
    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private UserAssembler userAssembler;

    public SingleResponse<TokenWebCO> execute(TokenRefreshCmd tokenRefreshCmd) {
        // 校验
        Assert.isTrue(jwtUtil.isAccessTokenExpired(tokenRefreshCmd.getAccessToken()), "访问令牌未过期");
        UserDto userDto = jwtUtil.verifierToken(tokenRefreshCmd.getRefreshToken(), true);
        Assert.isTrue(isNotEmpty(userDto), "刷新令牌无效");
        User user = extensionExecutor.execute(TokenRefreshExtPt.class,
                BizScenario.valueOf(USER_BIZ_ID, USER_TOKEN_REFRESH_USER_CASE), chk -> chk.exists(userDto));

        // 更新令牌
        UserDto dto = userAssembler.toJwtDto(user);
        TokenWebCO newTokenDto = new TokenWebCO();
        newTokenDto.setAccessToken(jwtUtil.createToken(dto, false));
        newTokenDto.setRefreshToken(jwtUtil.createToken(dto, true));

        log.info("用户刷新令牌成功，用户ID：{}", user.getId());
        return SingleResponse.of(newTokenDto);
    }
}
