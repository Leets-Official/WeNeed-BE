package org.example.weneedbe.domain.application.exception;

import org.example.weneedbe.global.error.ErrorCode;
import org.example.weneedbe.global.error.exception.ServiceException;

public class ApplicationNotFoundException extends ServiceException {
    public ApplicationNotFoundException() {
        super(ErrorCode.APPLICATION_NOT_FOUND_EXCEPTION);
    }
}
