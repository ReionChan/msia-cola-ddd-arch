package io.github.reionchan.payment.web;

import com.alibaba.cola.dto.Response;
import io.github.reionchan.payment.api.IWalletService;
import io.github.reionchan.payment.dto.command.WalletRechargeCmd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
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
@Tag(name = "WalletController", description = "钱包服务请求端点")
public class WalletController {

    private IWalletService walletService;

    public WalletController(IWalletService walletService) {
        this.walletService = walletService;
    }

    @PatchMapping("/wallet/recharge")
    @Operation(summary = "用户钱包充值", description = "用户钱包用户充值接口",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "WalletDto 传输对象",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    )
    @PreAuthorize("hasRole('user')")
    public Response recharge(@RequestBody @Validated WalletRechargeCmd walletRechargeCmd) {
        return walletService.updateWallet(walletRechargeCmd);
    }
}
