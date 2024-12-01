package io.github.reionchan.core.converter;

import io.github.reionchan.core.dto.RStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;

import static io.github.reionchan.core.consts.RStatusCommon.UNKNOWN;
import static io.github.reionchan.commons.validation.CommonUtil.isEmpty;

/**
 * @author Reion
 * @date 2024-06-04
 **/
public final class HttpStatusRStatusConverter implements Converter<HttpStatus, RStatus> {
    @Override
    public RStatus convert(@NotNull HttpStatus status) {
        if (isEmpty(status)) return UNKNOWN;
        return new RStatus() {
            @Override
            public int getCode() {
                return status.value();
            }

            @Override
            public String getReasonPhrase() {
                return status.getReasonPhrase();
            }

            @Override
            public boolean isSuccess() {
                return status.isError();
            }
        };
    }
}
