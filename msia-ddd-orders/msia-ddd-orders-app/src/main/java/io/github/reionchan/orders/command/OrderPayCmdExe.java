package io.github.reionchan.orders.command;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.Assert;
import com.alibaba.cola.extension.BizScenario;
import com.alibaba.cola.extension.ExtensionExecutor;
import io.github.reionchan.core.mq.MQManager;
import io.github.reionchan.mq.model.entity.MQMessage;
import io.github.reionchan.orders.convertor.Order2MessageConvertor;
import io.github.reionchan.orders.extentionpoint.OrderOwnerExtPt;
import io.github.reionchan.orders.extentionpoint.OrderPayExtPt;
import io.github.reionchan.orders.gateway.IOrderAggregateGateway;
import io.github.reionchan.orders.model.aggregate.OrderAggregate;
import io.github.reionchan.products.dto.StockDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static io.github.reionchan.orders.consts.OrdersBizScenarioCst.*;

/**
 * @author Reion
 * @date 2024-11-16
 **/
@Slf4j
@Component
public class OrderPayCmdExe {

    @Resource
    private IOrderAggregateGateway orderAggregateGateway;
    @Resource
    private ExtensionExecutor extensionExecutor;
    @Resource
    private MQManager mqManager;
    @Resource
    private Order2MessageConvertor messageConvertor;

    public Response execute(Long orderId) {
        OrderAggregate orderAggregate = orderAggregateGateway.getOrderAggById(orderId);
        // 校验
        extensionExecutor.executeVoid(OrderOwnerExtPt.class, BizScenario.valueOf(ALL_BIZ_ID, ALL_USE_CASE), chk -> chk.validate(orderAggregate.getOrder().getUserId()));
        // 业务
        Set<StockDTO> preOrderStockDtoSet = orderAggregate.getOrderDetailSet().stream()
            .map(detail -> {
                StockDTO stockDto = new StockDTO();
                stockDto.setId(detail.getId());
                stockDto.setProductId(detail.getProductId());
                stockDto.setName(detail.getProductName());
                stockDto.setAmount(detail.getAmount());
                return stockDto;
            }).collect(Collectors.toSet());
        Assert.isTrue(orderAggregate.payOrder(), "支付订单失败");
        // 保存消息
        Map<Long, Set<StockDTO>> pair = new HashMap<>();
        pair.put(orderId, preOrderStockDtoSet);
        MQMessage payMsg = messageConvertor.orderStock2Message(pair);
        Assert.isTrue(mqManager.save(payMsg), "支付成功消息保存失败");
        // 发送支付消息
        extensionExecutor.executeVoid(OrderPayExtPt.class, BizScenario.valueOf(ORDERS_BIZ_ID, ORDERS_PAY_USE_CASE), ext -> ext.notify(payMsg));

        log.info("订单：{} 已发起支付流程，稍后查询订单状态！", orderId);
        return Response.buildSuccess();
    }
}
