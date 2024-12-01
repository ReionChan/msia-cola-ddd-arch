package io.github.reionchan.core.rpc.feign;

import com.alibaba.cola.dto.SingleResponse;
import io.github.reionchan.core.config.FeignClientConfiguration;
import io.github.reionchan.payment.api.IPaymentRpc;
import io.github.reionchan.payment.dto.command.PaymentCreateCmd;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 支付 OpenFeign 客户端接口
 *
 * @author Reion
 * @date 2023-12-19
 **/
@FeignClient(name = "msia-payment",
        contextId = "msia-payment.PaymentFeignClient",
        configuration = FeignClientConfiguration.class)
public interface PaymentFeignClient extends IPaymentRpc {

    @Override
    @PostMapping("/rpc/payment")
    SingleResponse<Long> create(@RequestBody PaymentCreateCmd paymentCreateCmd);
}
