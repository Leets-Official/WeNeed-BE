package org.example.weneedbe.domain.article.exception;

import org.example.weneedbe.global.error.ErrorCode;
import org.example.weneedbe.global.error.exception.ServiceException;

public class AuthorMismatchException extends ServiceException {
  public AuthorMismatchException() {
    super(ErrorCode.AUTHOR_MISMATCH_ERROR);
  }
}
