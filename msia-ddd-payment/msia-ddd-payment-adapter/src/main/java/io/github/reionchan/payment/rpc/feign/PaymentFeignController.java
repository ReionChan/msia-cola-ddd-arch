package io.github.reionchan.payment.rpc.feign;

import com.alibaba.cola.dto.SingleResponse;
import io.github.reionchan.payment.api.IPaymentService;
import io.github.reionchan.payment.dto.command.PaymentCreateCmd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付控制器
 *
 * @author Reion
 * @date 2023-12-15
 **/
@RestController
@Validated
@Tag(name = "PaymentController", description = "支付服务请求端点")
public class PaymentFeignController {

    private IPaymentService paymentService;

    public PaymentFeignController(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/rpc/payment")
    @Operation(summary = "支付订单创建", description = "支付订单创建接口",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "PayInfoDto 传输对象",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    )
    @PreAuthorize("hasRole('user') && principal.id == #paymentCreateCmd.userId")
    public SingleResponse<Long> create(@RequestBody @Validated PaymentCreateCmd paymentCreateCmd) {
        return paymentService.createPayment(paymentCreateCmd);
    }
}
