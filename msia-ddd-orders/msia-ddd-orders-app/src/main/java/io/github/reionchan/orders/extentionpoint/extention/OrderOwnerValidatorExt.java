package io.github.reionchan.orders.extentionpoint.extention;

import com.alibaba.cola.exception.Assert;
import com.alibaba.cola.extension.Extension;
import io.github.reionchan.core.dto.UserDetailDto;
import io.github.reionchan.core.exception.UserNotLoginException;
import io.github.reionchan.orders.extentionpoint.OrderOwnerExtPt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static io.github.reionchan.commons.validation.CommonUtil.isEmpty;
import static io.github.reionchan.orders.consts.OrdersBizScenarioCst.ALL_BIZ_ID;
import static io.github.reionchan.orders.consts.OrdersBizScenarioCst.ALL_USE_CASE;

/**
 * @author Reion
 * @date 2024-11-20
 **/
@Slf4j
@Extension(bizId = ALL_BIZ_ID, useCase = ALL_USE_CASE)
public class OrderOwnerValidatorExt implements OrderOwnerExtPt {

    @Override
    public void validate(Long orderOwnerId) {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if (isEmpty(authentication)) {
            throw new UserNotLoginException("未登录");
        }
        UserDetailDto userDto = (UserDetailDto) authentication.getPrincipal();
        Assert.isTrue(orderOwnerId.equals(userDto.getId()), "无访问权限");
    }
}
