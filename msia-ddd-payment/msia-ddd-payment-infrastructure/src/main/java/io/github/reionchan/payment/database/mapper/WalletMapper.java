package io.github.reionchan.payment.database.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.reionchan.payment.database.dataobject.WalletDO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * 用户钱包实体映射
 *
 * @author Reion
 * @date 2023-12-13
 **/
public interface WalletMapper extends BaseMapper<WalletDO> {
    // 确保 userId 包含索引
    WalletDO getWalletForUpdate(@Param("userId") Long userId);

    int recharge(@Param("id") Long id, @Param("rechargeAmount") BigDecimal rechargeAmount);
}
