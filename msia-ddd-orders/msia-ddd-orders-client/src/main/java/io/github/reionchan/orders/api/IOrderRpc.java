package io.github.reionchan.orders.api;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import io.github.reionchan.orders.dto.OrderCreationDTO;

/**
 * 订单服务 RPC 接口
 *
 * @author Reion
 * @date 2023-12-20
 **/
public interface IOrderRpc {
    Response modify(Long id, Byte status);

    SingleResponse<OrderCreationDTO> getOrder(Long id);
}
