package io.github.reionchan.orders.web;


import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import io.github.reionchan.orders.api.IOrderService;
import io.github.reionchan.orders.dto.clientobject.OrderWebCO;
import io.github.reionchan.orders.dto.command.OrderCreationCmd;
import io.github.reionchan.orders.dto.command.OrderModifyCmd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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
public class OrderController {

    private IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order")
    @Operation(summary = "创建订单", description = "创建订单接口",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "OrderCreationDto 传输对象",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    )
    @PreAuthorize("hasRole('user') && principal.id == #orderCreationCmd.userId && principal.enabled")
    public SingleResponse<Long> create(@RequestBody @Validated OrderCreationCmd orderCreationCmd) {
        return orderService.createOrder(orderCreationCmd);
    }

    @PatchMapping("/order/product")
    @Operation(summary = "修改订单中商品数量", description = "用户未支付前修改商品数量接口",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "OrderDetailBatchModifyDto 传输对象",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    )
    @PreAuthorize("hasRole('user') && principal.enabled")
    public Response modifyOrderProducts(@RequestBody @Validated OrderModifyCmd batchModifyCmd) {
        return orderService.modifyOrderDetail(batchModifyCmd);
    }

    @PatchMapping("/order/{id}/pay")
    @Operation(summary = "订单支付", description = "用户支付订单接口")
    @PreAuthorize("hasRole('user') && principal.enabled")
    public Response pay(@PathVariable("id") @Validated @Positive(message = "id 必须大于 0") Long id) {
        return orderService.payOrder(id);
    }

    @DeleteMapping("/order/{id}")
    @Operation(summary = "删除订单", description = "用户删除订单接口，需未支付")
    @PreAuthorize("hasRole('user') && principal.enabled")
    public Response delete(@PathVariable("id") @Positive(message = "id 必须大于 0") Long id) {
        return orderService.deleteOrder(id);
    }

    @GetMapping("/order/{id}")
    @Operation(summary = "查询订单", description = "查询订单接口")
    @PreAuthorize("hasRole('user') && principal.enabled")
    public SingleResponse<OrderWebCO> getOrder(@PathVariable("id") @Positive(message = "id 必须大于 0") Long id) {
        return orderService.getOrder(id, false);
    }
}
