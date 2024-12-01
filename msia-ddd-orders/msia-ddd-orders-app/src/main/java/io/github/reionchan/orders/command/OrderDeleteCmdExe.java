package io.github.reionchan.orders.command;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.Assert;
import com.alibaba.cola.extension.BizScenario;
import com.alibaba.cola.extension.ExtensionExecutor;
import io.github.reionchan.orders.extentionpoint.OrderOwnerExtPt;
import io.github.reionchan.orders.gateway.IOrderAggregateGateway;
import io.github.reionchan.orders.model.aggregate.OrderAggregate;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static io.github.reionchan.orders.consts.OrdersBizScenarioCst.ALL_BIZ_ID;
import static io.github.reionchan.orders.consts.OrdersBizScenarioCst.ALL_USE_CASE;

/**
 * @author Reion
 * @date 2024-11-16
 **/
@Slf4j
@Component
public class OrderDeleteCmdExe {

    @Resource
    private IOrderAggregateGateway orderAggregateGateway;
    @Resource
    private ExtensionExecutor extensionExecutor;

    public Response execute(Long orderId) {
        OrderAggregate orderAggregate = orderAggregateGateway.getOrderAggById(orderId);
        // 校验
        extensionExecutor.executeVoid(OrderOwnerExtPt.class, BizScenario.valueOf(ALL_BIZ_ID, ALL_USE_CASE), chk -> chk.validate(orderAggregate.getOrder().getUserId()));
        // 业务
        Assert.isTrue(orderAggregate.delete(), "订单删除失败");
        return Response.buildSuccess();
    }
}
