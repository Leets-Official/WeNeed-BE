package org.example.weneedbe.domain.article.exception;

import org.example.weneedbe.global.error.ErrorCode;
import org.example.weneedbe.global.error.exception.ServiceException;

public class InvalidSortException extends ServiceException {
    public InvalidSortException() {
        super(ErrorCode.INVALID_SORT_ERROR);
    }
}
