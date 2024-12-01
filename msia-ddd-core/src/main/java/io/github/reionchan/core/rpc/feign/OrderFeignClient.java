package io.github.reionchan.core.rpc.feign;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import io.github.reionchan.core.config.FeignClientConfiguration;
import io.github.reionchan.orders.api.IOrderRpc;
import io.github.reionchan.orders.dto.OrderCreationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 订单 OpenFeign 客户端接口
 *
 * @author Reion
 * @date 2023-12-19
 **/
@FeignClient(name = "msia-orders", configuration = FeignClientConfiguration.class)
public interface OrderFeignClient extends IOrderRpc {
    @Override
    @PatchMapping("/rpc/order/{id}")
    Response modify(@PathVariable("id") Long id, @RequestParam("status") Byte status);

    @Override
    @GetMapping("/rpc/order/admin/{id}")
    SingleResponse<OrderCreationDTO> getOrder(@PathVariable("id") Long id);
}
