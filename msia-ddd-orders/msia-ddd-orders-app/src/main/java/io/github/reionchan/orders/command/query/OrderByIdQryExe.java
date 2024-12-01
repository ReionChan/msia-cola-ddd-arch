package io.github.reionchan.orders.command.query;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import com.alibaba.cola.exception.Assert;
import com.alibaba.cola.extension.BizScenario;
import com.alibaba.cola.extension.ExtensionExecutor;
import io.github.reionchan.orders.assembler.OrderAssembler;
import io.github.reionchan.orders.assembler.OrderDetailAssembler;
import io.github.reionchan.orders.dto.clientobject.OrderWebCO;
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
public class OrderByIdQryExe {

    @Resource
    private IOrderAggregateGateway orderAggregateGateway;
    @Resource
    private ExtensionExecutor extensionExecutor;
    @Resource
    private OrderAssembler orderAssembler;
    @Resource
    private OrderDetailAssembler detailAssembler;

    public SingleResponse<OrderWebCO> execute(Long orderId, boolean isAdmin) {
        OrderAggregate orderAggregate = orderAggregateGateway.getOrderAggById(orderId);
        // 校验
        if(!isAdmin) {
            extensionExecutor.executeVoid(OrderOwnerExtPt.class, BizScenario.valueOf(ALL_BIZ_ID, ALL_USE_CASE), chk -> chk.validate(orderAggregate.getOrder().getUserId()));
        }
        // 业务
        OrderWebCO orderWebCO = orderAssembler.toClientObject(orderAggregate.getOrder());
        orderWebCO.setOrderDetails(detailAssembler.toClientObjectSet(orderAggregate.getOrderDetailSet()));
        return SingleResponse.of(orderWebCO);
    }
}
