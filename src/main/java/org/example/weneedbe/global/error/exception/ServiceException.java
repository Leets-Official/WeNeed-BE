package org.example.weneedbe.global.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.weneedbe.global.error.ErrorCode;

@Getter
@AllArgsConstructor
public class ServiceException extends RuntimeException {
    private final ErrorCode errorCode;
}