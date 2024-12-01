package io.github.reionchan.core.exception;

/**
 * 业务异常
 *
 * @author Reion
 * @date 2023-06-06
 **/
public class BusinessException extends RuntimeException {

    private Object data;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Object data) {
        super(message);
        this.data = data;
    }

    public BusinessException(String message, Throwable cause, Object data) {
        super(message, cause);
        this.data = data;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.data = null;
    }

    public Object getData() {
        return data;
    }
}
