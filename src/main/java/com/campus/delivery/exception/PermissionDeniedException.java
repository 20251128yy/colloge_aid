package com.campus.delivery.exception;

/**
 * 权限不足异常
 */
public class PermissionDeniedException extends RuntimeException {
    public PermissionDeniedException() {
        super("权限不足");
    }

    public PermissionDeniedException(String message) {
        super(message);
    }
}
