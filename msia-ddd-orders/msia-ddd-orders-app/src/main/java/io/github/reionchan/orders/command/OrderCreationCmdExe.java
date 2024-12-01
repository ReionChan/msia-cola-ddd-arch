package io.github.reionchan.orders.command;

import com.alibaba.cola.domain.DomainFactory;
import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.SingleResponse;
import io.github.reionchan.orders.assembler.OrderAssembler;
import io.github.reionchan.orders.assembler.OrderDetailAssembler;
import io.github.reionchan.orders.dto.command.OrderCreationCmd;
import io.github.reionchan.orders.model.aggregate.OrderAggregate;
import io.github.reionchan.orders.model.entity.Order;
import io.github.reionchan.orders.model.entity.OrderDetail;
import io.github.reionchan.products.api.IProductRpc;
import io.github.reionchan.products.dto.clientobject.ProductWebCO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Reion
 * @date 2024-11-16
 **/
@Slf4j
@Component
public class OrderCreationCmdExe {

    @Resource
    private IProductRpc productRpc;
    @Resource
    private OrderAssembler orderAssembler;
    @Resource
    private OrderDetailAssembler detailAssembler;

    public SingleResponse<Long> execute(OrderCreationCmd cmd) {
        // 校验
        // 组装
        Order order = orderAssembler.toEntity(cmd);
        Set<OrderDetail> details = detailAssembler.toEntitySet(cmd.getOrderDetails());
        MultiResponse<ProductWebCO> productListRes = productRpc.getProductsByIds(
                cmd.getOrderDetails().stream().map(detail -> String.valueOf(detail.getProductId())).collect(Collectors.joining(",")));
        Assert.isTrue(productListRes.isSuccess(), productListRes.getErrMessage());
        Assert.isTrue(cmd.getOrderDetails().size() == productListRes.getData().size(), "有商品缺失信息");
        details.stream().forEach(detail -> {
            productListRes.getData().stream().filter(d -> d.getId().equals(detail.getProductId())).findFirst().ifPresent(
                dto -> {
                    detail.setProductName(dto.getName());
                    detail.setPrice(dto.getPrice());
                });
        });
        // 业务
        OrderAggregate orderAgg = DomainFactory.create(OrderAggregate.class);
        orderAgg.setOrder(order);
        orderAgg.setOrderDetailSet(details);
        Assert.isTrue(orderAgg.save(), "创建订单失败");
        log.info("新增 {} 订单成功！", orderAgg.getOrderId());
        return SingleResponse.of(orderAgg.getOrderId());
    }
}
