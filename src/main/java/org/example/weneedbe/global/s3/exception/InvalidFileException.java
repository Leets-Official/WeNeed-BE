package org.example.weneedbe.global.s3.exception;

import org.example.weneedbe.global.error.ErrorCode;
import org.example.weneedbe.global.error.exception.ServiceException;

public class InvalidFileException extends ServiceException {
    public InvalidFileException() {
        super(ErrorCode.INVALID_FILE);
    }
}
