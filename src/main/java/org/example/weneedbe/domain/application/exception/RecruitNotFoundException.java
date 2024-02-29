package org.example.weneedbe.domain.application.exception;

import org.example.weneedbe.global.error.ErrorCode;
import org.example.weneedbe.global.error.exception.ServiceException;

public class RecruitNotFoundException extends ServiceException {
  public RecruitNotFoundException() {
    super(ErrorCode.RECRUIT_NOT_FOUND_EXCEPTION);
  }
}
