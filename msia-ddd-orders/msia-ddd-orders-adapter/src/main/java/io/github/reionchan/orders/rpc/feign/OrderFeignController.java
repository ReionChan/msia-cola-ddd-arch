package io.github.reionchan.orders.rpc.feign;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import io.github.reionchan.orders.dto.OrderCreationDTO;
import io.github.reionchan.orders.api.IOrderService;
import io.github.reionchan.orders.dto.clientobject.OrderWebCO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 订单控制器
 *
 * @author Reion
 * @date 2023-12-15
 **/
@Slf4j
@Validated
@RestController
@Tag(name = "OrderController", description = "订单服务请求端点")
public class OrderFeignController {

    private IOrderService orderService;

    public OrderFeignController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @PatchMapping("/rpc/order/{id}")
    @Operation(summary = "修改订单状态", description = "修改订单接口")
    @PreAuthorize("hasRole('admin')")
    public Response modify(
            @PathVariable("id") @Positive(message = "id 必须大于 0") Long id,
            @RequestParam("status") @Range(min = 0, max = 9, message = "状态值不合法") Byte status) {
        return orderService.modifyOrderStatus(id, status);
    }

    @GetMapping("/rpc/order/admin/{id}")
    @Operation(summary = "系统管理员查询订单", description = "查询订单接口")
    @PreAuthorize("hasRole('admin')")
    public SingleResponse<OrderWebCO> getOrderByAdmin(@PathVariable("id") @Positive(message = "id 必须大于 0") Long id) {
        return orderService.getOrder(id, true);
    }
}
