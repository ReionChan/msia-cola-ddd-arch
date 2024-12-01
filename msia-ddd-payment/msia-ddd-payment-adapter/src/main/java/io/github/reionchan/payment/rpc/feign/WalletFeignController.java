package io.github.reionchan.payment.rpc.feign;

import com.alibaba.cola.dto.SingleResponse;
import io.github.reionchan.payment.api.IWalletService;
import io.github.reionchan.payment.dto.command.WalletCreateCmd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 钱包控制器
 *
 * @author Reion
 * @date 2023-12-15
 **/
@RestController
@Validated
@Tag(name = "WalletFeignController", description = "钱包RPC服务请求端点")
public class WalletFeignController {

    private IWalletService walletService;

    public WalletFeignController(IWalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/rpc/wallet")
    @Operation(summary = "用户钱包创建", description = "用户钱包创建接口",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "WalletDto 传输对象",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    )
    @PreAuthorize("hasRole('admin')")
    public SingleResponse<Long> create(@RequestBody @Validated WalletCreateCmd cmd) {
        return walletService.createWallet(cmd);
    }
}
