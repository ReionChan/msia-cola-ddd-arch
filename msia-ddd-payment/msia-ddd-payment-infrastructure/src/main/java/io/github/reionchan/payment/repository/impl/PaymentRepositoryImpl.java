package io.github.reionchan.payment.repository.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.reionchan.payment.convertor.PaymentEntityConvertor;
import io.github.reionchan.payment.database.dataobject.PaymentDO;
import io.github.reionchan.payment.database.mapper.PaymentMapper;
import io.github.reionchan.payment.dto.command.PaymentCreateCmd;
import io.github.reionchan.payment.model.entity.Payment;
import io.github.reionchan.payment.model.vo.PaymentStatus;
import io.github.reionchan.payment.repository.IPaymentRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static io.github.reionchan.commons.validation.CommonUtil.isNotEmpty;

/**
 * @author Reion
 * @date 2024-11-21
 **/
@Repository
public class PaymentRepositoryImpl implements IPaymentRepository {

    @Resource
    private PaymentMapper paymentMapper;
    @Resource
    private PaymentEntityConvertor paymentEntityConvertor;

    @Override
    public boolean existsPayment(PaymentCreateCmd cmd) {
        Wrapper<PaymentDO> query = new LambdaQueryWrapper<PaymentDO>()
                .eq(PaymentDO::getUserId, cmd.getUserId())
                .eq(PaymentDO::getOrderId, cmd.getOrderId());
        return paymentMapper.exists(query);
    }

    @Override
    public boolean save(Payment payment) {
        PaymentDO paymentDO = paymentEntityConvertor.toDataObject(payment);
        paymentDO.setCreateTime(new Date());
        paymentDO.setUpdateTime(paymentDO.getCreateTime());
        boolean flag = paymentMapper.insert(paymentDO) > 0;
        if(flag) {
            payment.setId(paymentDO.getId());
        }
        return flag;
    }

    @Override
    public List<Payment> getPaying() {
        List<PaymentDO> toPayList = paymentMapper.selectList(new LambdaQueryWrapper<PaymentDO>()
                .eq(PaymentDO::getStatus, PaymentStatus.PAYING.getValue())
                .or(w -> w.isNull(PaymentDO::getStatus)));
        return isNotEmpty(toPayList) ? paymentEntityConvertor.toEntityList(toPayList) : null;
    }

    @Override
    public boolean updateById(Payment payment) {
        PaymentDO paymentDO = paymentEntityConvertor.toDataObject(payment);
        return paymentMapper.updateById(paymentDO)>0;
    }
}
