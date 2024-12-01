package io.github.reionchan.users.command.query;

import com.alibaba.cola.dto.SingleResponse;
import io.github.reionchan.core.dto.UserDto;
import io.github.reionchan.core.security.JwtUtil;
import io.github.reionchan.users.assembler.UserAssembler;
import io.github.reionchan.users.dto.clientobject.TokenWebCO;
import io.github.reionchan.users.dto.command.query.UserLoginQry;
import io.github.reionchan.users.model.entity.User;
import io.github.reionchan.users.repository.IUserRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author Reion
 * @date 2024-11-16
 **/
@Slf4j
@Component
public class UserLoginQryExe {

    @Resource
    private IUserRepository userRepository;
    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private UserAssembler userAssembler;


    public SingleResponse<TokenWebCO> execute(UserLoginQry qry) {
        // 查询
        User user = userRepository.queryByUserName(qry.getUserName());
        Assert.notNull(user, qry.getUserName() + " 不存在");
        // 业务
        Assert.isTrue(user.login(qry.getPassword()), "密码错误");
        // 后置处理
        UserDto dto = userAssembler.toJwtDto(user);
        TokenWebCO tokenDto = new TokenWebCO();
        tokenDto.setAccessToken(jwtUtil.createToken(dto, false));
        tokenDto.setRefreshToken(jwtUtil.createToken(dto, true));
        log.info("用户登录成功，用户ID：{}", user.getId());

        return SingleResponse.of(tokenDto);
    }
}
