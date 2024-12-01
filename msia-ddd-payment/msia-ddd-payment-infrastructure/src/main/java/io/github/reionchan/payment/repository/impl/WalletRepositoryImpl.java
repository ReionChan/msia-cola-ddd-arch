package io.github.reionchan.payment.repository.impl;

import com.alibaba.cola.exception.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.reionchan.payment.convertor.WalletEntityConvertor;
import io.github.reionchan.payment.database.dataobject.WalletDO;
import io.github.reionchan.payment.database.mapper.WalletMapper;
import io.github.reionchan.payment.model.entity.Wallet;
import io.github.reionchan.payment.repository.IWalletRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;

import static io.github.reionchan.commons.validation.CommonUtil.isEmpty;
import static io.github.reionchan.commons.validation.CommonUtil.isNotEmpty;

/**
 * @author Reion
 * @date 2024-11-21
 **/
@Repository
public class WalletRepositoryImpl implements IWalletRepository {

    @Resource
    private WalletMapper walletMapper;
    @Resource
    private WalletEntityConvertor walletEntityConvertor;

    public Long existByUserId(Long userId) {
        Assert.notNull(userId, "用户ID不能为空");
        QueryWrapper<WalletDO> queryWrapper = new QueryWrapper<>();
        WalletDO walletDO = walletMapper.selectOne(queryWrapper.lambda().eq(WalletDO::getUserId, userId));
        return isNotEmpty(walletDO) ? walletDO.getId() : null;
    }

    @Override
    public Wallet existByWalletId(Long id) {
        Assert.notNull(id, "钱包ID不能为空");
        QueryWrapper<WalletDO> queryWrapper = new QueryWrapper<>();
        WalletDO walletDO = walletMapper.selectOne(queryWrapper.lambda().eq(WalletDO::getId, id));
        return isNotEmpty(walletDO) ? walletEntityConvertor.toEntity(walletDO) : null;
    }

    @Override
    public boolean save(Wallet wallet) {
        WalletDO walletDO = walletEntityConvertor.toDataObject(wallet);
        walletDO.setCreateTime(new Date());
        walletDO.setUpdateTime(walletDO.getCreateTime());
        boolean flag = walletMapper.insert(walletDO) > 0;
        wallet.setId(walletDO.getId());
        return flag;
    }

    @Override
    public boolean recharge(Long id, BigDecimal rechargeAmount) {
        return walletMapper.recharge(id, rechargeAmount) > 0;
    }

    @Override
    public Wallet getWalletForUpdate(Long userId) {
        Assert.notNull(userId, "用户ID不能为空");
        WalletDO walletDO = walletMapper.getWalletForUpdate(userId);
        if(isEmpty(walletDO)) return null;
        return walletEntityConvertor.toEntity(walletDO);
    }

    @Override
    public boolean updateById(Wallet wallet) {
        WalletDO walletDO = walletEntityConvertor.toDataObject(wallet);
        walletDO.setUpdateTime(new Date());
        return walletMapper.updateById(walletDO) > 0;
    }
}
