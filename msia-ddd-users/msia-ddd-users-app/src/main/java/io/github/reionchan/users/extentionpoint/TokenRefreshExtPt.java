package io.github.reionchan.users.extentionpoint;

import com.alibaba.cola.extension.ExtensionPointI;
import io.github.reionchan.core.dto.UserDto;
import io.github.reionchan.users.dto.command.TokenRefreshCmd;
import io.github.reionchan.users.dto.command.UserModifyCmd;
import io.github.reionchan.users.model.entity.User;

/**
 * 用户是否存在扩展点
 *
 * @author Reion
 * @date 2024-11-20
 **/
public interface TokenRefreshExtPt extends ExtensionPointI {
    User exists(UserDto dto);
}
