package io.github.reionchan.orders.service;

import com.alibaba.cola.catchlog.CatchAndLog;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import io.github.reionchan.orders.api.IOrderService;
import io.github.reionchan.orders.command.OrderCreationCmdExe;
import io.github.reionchan.orders.command.OrderDeleteCmdExe;
import io.github.reionchan.orders.command.OrderModifyCmdExe;
import io.github.reionchan.orders.command.OrderPayCmdExe;
import io.github.reionchan.orders.command.query.OrderByIdQryExe;
import io.github.reionchan.orders.dto.clientobject.OrderWebCO;
import io.github.reionchan.orders.dto.command.OrderCreationCmd;
import io.github.reionchan.orders.dto.command.OrderModifyCmd;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单服务实现类
 *
 * @author Reion
 * @date 2023-12-16
 **/
@Slf4j
@Service
@CatchAndLog
public class OrderServiceImpl implements IOrderService {

    @Resource
    private OrderCreationCmdExe orderCreationCmdExe;
    @Resource
    private OrderModifyCmdExe orderModifyCmdExe;
    @Resource
    private OrderDeleteCmdExe orderDeleteCmdExe;
    @Resource
    private OrderByIdQryExe orderByIdQryExe;
    @Resource
    private OrderPayCmdExe orderPayCmdExe;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public SingleResponse<Long> createOrder(OrderCreationCmd cmd) {
        return orderCreationCmdExe.execute(cmd);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response modifyOrderDetail(OrderModifyCmd batchModifyCmd) {
        return orderModifyCmdExe.execute(batchModifyCmd);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response deleteOrder(Long id) {
        return orderDeleteCmdExe.execute(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response payOrder(Long id) {
        return orderPayCmdExe.execute(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response modifyOrderStatus(Long id, Byte status) {
        return orderModifyCmdExe.execute(id, status);
    }

    @Override
    public SingleResponse<OrderWebCO> getOrder(Long id, boolean isAdmin) {
        return orderByIdQryExe.execute(id, isAdmin);
    }
}
