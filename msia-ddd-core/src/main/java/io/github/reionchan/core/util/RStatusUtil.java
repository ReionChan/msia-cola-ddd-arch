package io.github.reionchan.core.util;

import io.github.reionchan.core.converter.HttpStatusRStatusConverter;
import io.github.reionchan.core.dto.RStatus;
import org.springframework.http.HttpStatus;

/**
 * @author Reion
 * @date 2024-06-04
 **/
public final class RStatusUtil {
    private static final HttpStatusRStatusConverter CONVERTER = new HttpStatusRStatusConverter();
    public static RStatus of(HttpStatus status) {
        return CONVERTER.convert(status);
    }
}
