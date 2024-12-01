package io.github.reionchan.payment.api;

import com.alibaba.cola.dto.SingleResponse;
import io.github.reionchan.payment.dto.PaymentDTO;
import io.github.reionchan.payment.dto.command.PaymentCreateCmd;

import java.util.List;

/**
 * 支付服务
 *
 * @author Reion
 * @date 2023-12-16
 **/
public interface IPaymentService {
    SingleResponse<Long> createPayment(PaymentCreateCmd paymentCreateCmd);

    void payedInTransaction(PaymentDTO payment) throws RuntimeException;

    List<PaymentDTO> getPayingList();
}
