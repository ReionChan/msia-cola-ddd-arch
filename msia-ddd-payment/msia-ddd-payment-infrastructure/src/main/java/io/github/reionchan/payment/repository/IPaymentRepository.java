package io.github.reionchan.payment.repository;

import io.github.reionchan.payment.dto.command.PaymentCreateCmd;
import io.github.reionchan.payment.model.entity.Payment;

import java.util.List;

/**
 * @author Reion
 * @date 2024-11-21
 **/
public interface IPaymentRepository {

    boolean existsPayment(PaymentCreateCmd cmd);

    boolean save(Payment payment);

    List<Payment> getPaying();

    boolean updateById(Payment payment);
}
