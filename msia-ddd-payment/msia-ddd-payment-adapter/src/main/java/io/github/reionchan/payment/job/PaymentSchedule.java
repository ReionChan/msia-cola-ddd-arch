package io.github.reionchan.payment.job;

import io.github.reionchan.payment.api.IPaymentService;
import io.github.reionchan.payment.dto.PaymentDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.github.reionchan.commons.validation.CommonUtil.isEmpty;

/**
 * 支付调度
 *
 * @author Reion
 * @date 2023-12-19
 **/
@Slf4j
@Component
public class PaymentSchedule {

    @Resource
    private IPaymentService paymentService;

    @Scheduled(cron = "${payment.pay-order-schedule:0/10 * * * * ?}")
    public void payedSchedule() {
        List<PaymentDTO> toPayList = paymentService.getPayingList();

        log.debug("待支付列表大小：{}", isEmpty(toPayList) ? 0 : toPayList.size());
        if (isEmpty(toPayList)) return;

        for (PaymentDTO payment : toPayList) {
            try {
                paymentService.payedInTransaction(payment);
            } catch (Exception e) {
                log.error("支付ID：{} 支付失败，原因：{}", payment.getId(), e.getMessage());
            }
        }
    }
}
