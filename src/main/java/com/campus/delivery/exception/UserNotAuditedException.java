package com.campus.delivery.exception;

/**
 * 用户未审核异常
 */
public class UserNotAuditedException extends RuntimeException {
    public UserNotAuditedException() {
        super("用户未审核通过");
    }

    public UserNotAuditedException(String message) {
        super(message);
    }
}
