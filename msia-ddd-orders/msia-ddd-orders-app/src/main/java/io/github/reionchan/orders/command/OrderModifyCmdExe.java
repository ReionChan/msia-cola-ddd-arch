package io.github.reionchan.orders.command;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.Assert;
import com.alibaba.cola.extension.BizScenario;
import com.alibaba.cola.extension.ExtensionExecutor;
import io.github.reionchan.orders.dto.command.OrderModifyCmd;
import io.github.reionchan.orders.extentionpoint.OrderOwnerExtPt;
import io.github.reionchan.orders.gateway.IOrderAggregateGateway;
import io.github.reionchan.orders.model.aggregate.OrderAggregate;
import io.github.reionchan.orders.model.entity.OrderDetail;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static io.github.reionchan.orders.consts.OrdersBizScenarioCst.ALL_BIZ_ID;
import static io.github.reionchan.orders.consts.OrdersBizScenarioCst.ALL_USE_CASE;

/**
 * @author Reion
 * @date 2024-11-16
 **/
@Slf4j
@Component
public class OrderModifyCmdExe {

    @Resource
    private IOrderAggregateGateway orderAggregateGateway;
    @Resource
    private ExtensionExecutor extensionExecutor;

    public Response execute(OrderModifyCmd cmd) {
        OrderAggregate orderAggregate = orderAggregateGateway.getOrderAggById(cmd.getId());
        // 校验
        extensionExecutor.executeVoid(OrderOwnerExtPt.class, BizScenario.valueOf(ALL_BIZ_ID, ALL_USE_CASE), chk -> chk.validate(orderAggregate.getOrder().getUserId()));
        // 业务
        Set<OrderDetail> oldSet = orderAggregate.getOrderDetailSet();
        Set<OrderDetail> needRemoveSet = new HashSet<>();
        boolean[] needUpdate = {false};
        cmd.getOrderDetails().forEach(chg -> {
            if (chg.getAmount() > 0) {
                oldSet.stream().filter(d -> d.getProductId().equals(chg.getProductId())).findFirst().ifPresent(toUpdate -> {
                    if (!toUpdate.getAmount().equals(chg.getAmount())) {
                        toUpdate.setAmount(chg.getAmount());
                        needUpdate[0] = true;
                    }
                });
            } else {
                oldSet.stream().filter(d -> d.getProductId().equals(chg.getProductId())).findFirst().ifPresent(toDelete -> {
                    oldSet.remove(toDelete);
                    needRemoveSet.add(toDelete);
                });
            }
        });
        if (needUpdate[0] || needRemoveSet.size()>0) {
            Assert.isTrue(orderAggregate.update(needRemoveSet), "修改订单失败");
        }
        return Response.buildSuccess();
    }

    public Response execute(Long orderId, Byte status) {
        OrderAggregate orderAggregate = orderAggregateGateway.getOrderAggById(orderId);
        // 业务
        Assert.isTrue(orderAggregate.updateStatus(status), "修改订单失败");
        return Response.buildSuccess();
    }
}
