package io.github.reionchan.payment.gateway;

import io.github.reionchan.payment.repository.IPaymentRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @author Reion
 * @date 2024-11-21
 **/
@Component
public class PaymentGatewayImpl implements IPaymentGateway {
    @Resource
    private IPaymentRepository paymentRepository;
}
