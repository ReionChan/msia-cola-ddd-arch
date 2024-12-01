package io.github.reionchan.products.model.entity;

import com.alibaba.cola.domain.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品持久化类
 *
 * @author Reion
 * @date 2023-04-25
 **/
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "barCode"})
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String barCode;

    private BigDecimal price;
}
