package io.github.reionchan.core.rpc.feign;

import com.alibaba.cola.dto.MultiResponse;
import io.github.reionchan.core.config.FeignClientConfiguration;
import io.github.reionchan.products.api.IProductRpc;
import io.github.reionchan.products.dto.clientobject.ProductWebCO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 产品 OpenFeign 客户端接口
 *<pre>
 * 1. @FeignClient 注解生成 OpenFeign 客户端代理 Bean 的名称生成规则为：
 *  {@code FeignClientsRegistrar#getClientName(Map)}
 *  只提供 name 属性时，Bean 名称为：msia-products.FeignClientSpecification
 *  如果 msia-products 上有多个 FeignClient 注解时，就会在当前容器上下文发生重名名冲突，抛出异常：
 *  The bean 'msia-products.FeignClientSpecification' could not be registered.
 *  解决办法也简单，指定不同的 contextId 即可。
 *
 * 2. @FeignClient 注解的接口，只被允许继承一个接口，否则生成代理时会报错
 *
 * </pre>
 * @author Reion
 * @date 2023-12-19
 **/
@FeignClient(name = "msia-products",
        contextId = "msia-products.ProductFeignClient",
        configuration = FeignClientConfiguration.class)
public interface ProductFeignClient extends IProductRpc {

    @Override
    @GetMapping("/rpc/products")
    MultiResponse<ProductWebCO> getProductsByIds(@RequestParam("ids") String ids);
}
