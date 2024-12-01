package io.github.reionchan.core.exception;

/**
 * 用户无权限
 *
 * @author Reion
 * @date 2023-06-06
 **/
public class UserNotPermitException extends RuntimeException {
    public UserNotPermitException(String message) {
        super(message);
    }

    public UserNotPermitException(String message, Throwable cause) {
        super(message, cause);
    }
}
