package io.github.reionchan.core.consts;

/**
 * 分布式锁-键名常量
 *
 * @author Reion
 * @date 2023-12-29
 **/
public interface LockKeys {
    /**
     * 商品库存锁
     */
    String KEY_CHANGE_STOCK = "change-stock";
    String KEY_PAY = "wallet-pay-%d";
}
