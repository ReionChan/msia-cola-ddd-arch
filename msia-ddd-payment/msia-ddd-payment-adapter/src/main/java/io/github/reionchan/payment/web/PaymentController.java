package io.github.reionchan.payment.web;

import io.github.reionchan.payment.api.IPaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
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
public class PaymentController {

    private IPaymentService paymentService;

    public PaymentController(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
