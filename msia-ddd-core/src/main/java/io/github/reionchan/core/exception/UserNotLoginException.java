package io.github.reionchan.core.exception;

/**
 * 用户未登录
 *
 * @author Reion
 * @date 2023-06-06
 **/
public class UserNotLoginException extends RuntimeException {
    public UserNotLoginException(String message) {
        super(message);
    }

    public UserNotLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
