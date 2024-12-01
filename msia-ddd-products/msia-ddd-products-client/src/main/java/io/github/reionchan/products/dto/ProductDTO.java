package io.github.reionchan.products.dto;

import com.alibaba.cola.dto.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品数据传输对象
 *
 * @author Reion
 * @date 2023-12-14
 **/
@Data
@Schema(name = "ProductDto", description = "产品数据传输对象")
public class ProductDTO extends DTO {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "id 不能为空")
    @Positive(message = "id 必须大于 0")
    @Schema(title = "产品ID", description = "产品唯一ID")
    private Long id;

    @NotNull(message = "产品名称不能为空")
    @Min(value = 1, message = "产品名称长度最小 1")
    @Max(value = 32, message = "产品名称长度最大 32")
    @Schema(title = "产品名称", description = "长度1~32",  minLength = 1, maxLength = 32)
    private String name;

    @NotNull(message = "产品价格不能为空")
    @DecimalMin(value = "0.01", message = "产品价格必须不小于 0.01")
    @Digits(integer = 10, fraction = 2, message = "整数最长 10 位，小数最长 2 位")
    @Schema(title = "产品价格", description = "整数最长 10 位，小数最长 2 位，必须不小于 0.01，单位：元")
    private BigDecimal price;

    @Pattern(regexp = "[0-9]{6,32}", message = "产品条码长度6~32的数字字符串")
    @NotNull(message = "产品条码不能为空")
    @Schema(title = "产品条码", description = "长度6~32的数字字符串",  minLength = 6, maxLength = 32)
    private String barCode;

    @Schema(title = "创建时间，格式：yyyy-MM-dd hh:mm:ss")
    private Date createTime;

    @Schema(title = "修改时间，格式：yyyy-MM-dd hh:mm:ss")
    private Date updateTime;
}
