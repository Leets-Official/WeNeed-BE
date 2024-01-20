package org.example.weneedbe.global.jwt.exception;

import org.example.weneedbe.global.error.ErrorCode;
import org.example.weneedbe.global.error.exception.ServiceException;

public class InvalidTokenException extends ServiceException {
    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }
}
