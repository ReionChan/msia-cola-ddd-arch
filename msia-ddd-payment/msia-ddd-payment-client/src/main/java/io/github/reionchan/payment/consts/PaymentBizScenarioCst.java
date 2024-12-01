package io.github.reionchan.payment.consts;

/**
 * @author Reion
 * @date 2024-11-21
 **/
public interface PaymentBizScenarioCst {
    // 全业务
    String ALL_BIZ_ID = "all";
    // 全用例
    String ALL_USE_CASE = "all";

    // 支付业务
    String PAYMENT_BIZ_ID = "payment";
    // 支付钱包用例
    String PAYMENT_WALLET_USE_CASE = "wallet";
    // 支付钱包创建场景
    String PAYMENT_WALLET_CREATE_SCENARIO = "create";
    // 支付钱包充值场景
    String PAYMENT_WALLET_RECHARGE_SCENARIO = "recharge";

    // 支付钱包用例
    String PAYMENT_PAY_USE_CASE = "pay";
    // 支付钱包创建场景
    String PAYMENT_PAY_CREATE_SCENARIO = "create";
}
