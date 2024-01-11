package org.example.weneedbe.domain.user.exception;

import org.example.weneedbe.global.error.ErrorCode;
import org.example.weneedbe.global.error.exception.ServiceException;

public class UserNotFoundException extends ServiceException {
  public UserNotFoundException() {
    super(ErrorCode.USER_NOT_FOUND);
  }
}
