package org.example.weneedbe.domain.user.exception;

import org.example.weneedbe.global.error.ErrorCode;
import org.example.weneedbe.global.error.exception.ServiceException;

public class InvalidProfileEditException extends ServiceException {
    public InvalidProfileEditException() {
        super(ErrorCode.INVALID_EDIT_VALUE);
    }
}
