package io.github.reionchan.payment.model.entity;

import com.alibaba.cola.domain.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户钱包持久化类
 *
 * @author Reion
 * @date 2023-04-25
 **/
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "userId"})
public class Wallet implements Serializable {

    static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private BigDecimal availableBalance;

    private BigDecimal blockedBalance;
}
