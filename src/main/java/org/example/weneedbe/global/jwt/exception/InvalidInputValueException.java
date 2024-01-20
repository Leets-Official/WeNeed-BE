package org.example.weneedbe.global.jwt.exception;

import org.example.weneedbe.global.error.ErrorCode;
import org.example.weneedbe.global.error.exception.ServiceException;

public class InvalidInputValueException extends ServiceException {
    public InvalidInputValueException() {
        super(ErrorCode.INVALID_INPUT_VALUE);
    }
}
