package com.moneylover.Infrastructure.Exceptions;

public class NotFoundException extends BaseException {
    public NotFoundException() {
        super("RESOURCE_NOT_FOUND", 404);
    }
}
