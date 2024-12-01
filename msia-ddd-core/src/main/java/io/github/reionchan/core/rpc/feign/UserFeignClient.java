package io.github.reionchan.core.rpc.feign;

import com.alibaba.cola.dto.SingleResponse;
import io.github.reionchan.core.config.FeignClientConfiguration;
import io.github.reionchan.users.api.IUserRpc;
import io.github.reionchan.users.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 用户 OpenFeign 客户端接口
 *
 * @author Reion
 * @date 2023-12-19
 **/
@FeignClient(name = "msia-users", configuration = FeignClientConfiguration.class)
public interface UserFeignClient extends IUserRpc {

    @Override
    @GetMapping("/rpc/user/{id}")
    SingleResponse<UserDTO> getUserById(@PathVariable("id") Long id);
}
