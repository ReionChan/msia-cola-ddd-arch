package io.github.reionchan.payment.command.query;

import com.alibaba.cola.extension.ExtensionExecutor;
import io.github.reionchan.payment.assembler.PaymentAssembler;
import io.github.reionchan.payment.dto.PaymentDTO;
import io.github.reionchan.payment.model.entity.Payment;
import io.github.reionchan.payment.repository.IPaymentRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.github.reionchan.commons.validation.CommonUtil.isNotEmpty;

/**
 * @author Reion
 * @date 2024-11-16
 **/
@Slf4j
@Component
public class PaymentToPayQryExe {

    @Resource
    private IPaymentRepository paymentRepository;
    @Resource
    private PaymentAssembler paymentAssembler;
    @Resource
    private ExtensionExecutor extensionExecutor;

    public List<PaymentDTO> execute() {
        // 业务
        List<Payment> payingList = paymentRepository.getPaying();
        return isNotEmpty(payingList) ? paymentAssembler.toDTOList(payingList) : null;
    }
}
