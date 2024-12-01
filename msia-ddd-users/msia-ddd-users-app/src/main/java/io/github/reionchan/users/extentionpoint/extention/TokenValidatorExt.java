package io.github.reionchan.users.extentionpoint.extention;

import com.alibaba.cola.exception.Assert;
import com.alibaba.cola.extension.Extension;
import io.github.reionchan.core.dto.UserDto;
import io.github.reionchan.users.extentionpoint.TokenRefreshExtPt;
import io.github.reionchan.users.model.entity.User;
import io.github.reionchan.users.repository.impl.UserRepositoryImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import static io.github.reionchan.commons.validation.CommonUtil.isNotEmpty;
import static io.github.reionchan.users.consts.UsersBizScenarioCst.*;

/**
 * @author Reion
 * @date 2024-11-20
 **/
@Slf4j
@Extension(bizId = USER_BIZ_ID, useCase = USER_TOKEN_REFRESH_USER_CASE)
public class TokenValidatorExt implements TokenRefreshExtPt {

    @Resource
    private UserRepositoryImpl userRepository;

    @Override
    public User exists(UserDto dto) {
        // 查询用户
        User user = userRepository.queryById(dto.getId());
        Assert.isTrue(isNotEmpty(user) && user.isEnabled(), "用户不存在或已冻结");
        return user;
    }
}
