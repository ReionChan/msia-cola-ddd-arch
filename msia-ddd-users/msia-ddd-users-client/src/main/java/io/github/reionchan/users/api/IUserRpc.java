package io.github.reionchan.users.api;

import com.alibaba.cola.dto.SingleResponse;
import io.github.reionchan.users.dto.UserDTO;

/**
 * 用户 RPC 接口
 *
 * @author Reion
 * @date 2023-12-20
 **/
public interface IUserRpc {
    SingleResponse<UserDTO> getUserById(Long id);
}
